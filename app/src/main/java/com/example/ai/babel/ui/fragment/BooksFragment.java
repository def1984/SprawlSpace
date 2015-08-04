package com.example.ai.babel.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.CollectionBookAdapter;
import com.example.ai.babel.ui.AddNewBook;
import com.example.ai.babel.ui.EditBook;
import com.example.ai.babel.ui.PageActivity;
import com.example.ai.babel.ui.widget.MyFloatingActionButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class BooksFragment extends Fragment {

    private List<AVObject> bookListAll;
    private Button  BtnBgc;
    private MyFloatingActionButton fabBtn;
    private LinearLayout mLinearLayout;
    private int deleteIndex;
    private Boolean isCheck = false;
    private AVUser currentUser = AVUser.getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        fabBtnAm();
        addNewBook();
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadBooks().execute();
    }

    private void showAllMinFab() {
        Animation minFabSet = AnimationUtils.loadAnimation(getActivity(), R.anim.min_fab_anim);
        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.mini_fab_content);
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            LinearLayout mini = (LinearLayout) mLinearLayout.getChildAt(i);
            mini.setVisibility(View.VISIBLE);
            mini.startAnimation(minFabSet);
        }
    }

    private void hideAllMinFab() {
        mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.mini_fab_content);
        Animation minFabSetRve = AnimationUtils.loadAnimation(getActivity(), R.anim.min_fab_anim_rev);
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            LinearLayout mini = (LinearLayout) mLinearLayout.getChildAt(i);
            minFabSetRve.setFillAfter(true);
            mini.startAnimation(minFabSetRve);
            mini.setVisibility(View.GONE);
        }
    }
    private void fabBtnAm() {
        fabBtn = (MyFloatingActionButton) getActivity().findViewById(R.id.fab);
        BtnBgc = (Button) getActivity().findViewById(R.id.background_cover);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animSetFab = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_anim);
                Animation animSetCbg = AnimationUtils.loadAnimation(getActivity(), R.anim.background_cover_anim);
                ImageView fabImageView = (ImageView) getActivity().findViewById(R.id.img_fab);
                if (!isCheck) {
                    animSetFab.setFillAfter(true);
                    animSetCbg.setFillAfter(true);
                    showAllMinFab();
                    BtnBgc.setVisibility(View.VISIBLE);
                    BtnBgc.startAnimation(animSetCbg);
                    fabImageView.startAnimation(animSetFab);
                    isCheck = true;
                } else {
                    animSetFab.setInterpolator(new ReverseInterpolator());
                    animSetCbg.setInterpolator(new ReverseInterpolator());
                    fabImageView.startAnimation(animSetFab);
                    BtnBgc.startAnimation(animSetCbg);
                    BtnBgc.setVisibility(View.GONE);
                    hideAllMinFab();
                    isCheck = false;
                }
            }
        });

        BtnBgc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animSetFab = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_anim);
                Animation animSetCbg = AnimationUtils.loadAnimation(getActivity(), R.anim.background_cover_anim);
                ImageView fabImageView = (ImageView) getActivity().findViewById(R.id.img_fab);
                if (!isCheck) {
                    animSetFab.setFillAfter(true);
                    animSetCbg.setFillAfter(true);
                    showAllMinFab();
                    BtnBgc.setVisibility(View.VISIBLE);
                    BtnBgc.startAnimation(animSetCbg);
                    fabImageView.startAnimation(animSetFab);
                    isCheck = true;
                } else {
                    animSetFab.setInterpolator(new ReverseInterpolator());
                    animSetCbg.setInterpolator(new ReverseInterpolator());
                    fabImageView.startAnimation(animSetFab);
                    BtnBgc.startAnimation(animSetCbg);
                    BtnBgc.setVisibility(View.GONE);
                    hideAllMinFab();
                    isCheck = false;
                }
            }
        });
    }

    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.abs(paramFloat - 1f);
        }
    }

    private void addNewBook() {
        ImageButton addNewBook = (ImageButton) getActivity().findViewById(R.id.add_new_book);
        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewBook.class));
                getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                getActivity().finish();
            }
        });
    }

    public class LoadBooks extends AsyncTask<Void, Integer, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        URL picUrl = null;
        Bitmap pngBM = null;

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            AVQuery<AVObject> query = AVQuery.getQuery("Book");
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
            query.whereEqualTo("userObjectId", currentUser);
            query.orderByDescending("createdAt");
            try {
                bookListAll = query.find();
            } catch (AVException e) {
                Toast.makeText(getActivity(), "连接超时:错误代码:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            try {
                if (currentUser.getAVFile("AvatarImage") != null) {
                    picUrl = new URL(currentUser.getAVFile("AvatarImage").getUrl());
                } else {
                    picUrl = new URL("http://ac-9lv2ouk1.clouddn.com/AscMwdlOVcH3yBgT7GYbKTfeqOa52RCRQYHIQkT6.png");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                pngBM = BitmapFactory.decodeStream(picUrl.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("当前下载进度：" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            final GridView gridView = (GridView) getActivity().findViewById(R.id.grid_view);
            gridView.setAdapter(new CollectionBookAdapter(getActivity(), bookListAll));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent();
                    intent.putExtra("bookObjectId", bookListAll.get(i).getObjectId());
                    intent.setClass(getActivity(), PageActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);

                }
            });

            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    deleteIndex = i;
                    gridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                            contextMenu.add(0, 0, 0, "编辑封面");
                            contextMenu.add(0, 1, 0, "删除");
                        }
                    });
                    return false;
                }
            });

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent();
                intent.putExtra("bookObjectId", bookListAll.get(deleteIndex).getObjectId());
                intent.setClass(getActivity(), EditBook.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                getActivity().finish();
                break;
            case 1:
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        AVQuery<AVObject> pageQuery = AVQuery.getQuery("Page");
                        pageQuery.whereEqualTo("bookObjectId",bookListAll.get(deleteIndex).getObjectId());
                        pageQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
                        //pageQuery.whereEqualTo("bookObjectId", bookListAll.get(mViewPager.getCurrentItem()));
                        pageQuery.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (e == null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        list.get(i).deleteInBackground();
                                    }
                                    bookListAll.get(deleteIndex).deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                            new LoadBooks().execute();
                                        }
                                    });
                                }
                            }
                        });

                    }
                }, 1);

        }
        return super.onContextItemSelected(item);
    }
}
