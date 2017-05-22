package androidTest;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mla.newsapp.MainActivity;
import com.mla.newsapp.R;
import com.mla.newsapp.adapters.CommentAdapter;
import com.mla.newsapp.views.Comments;

/**
 * Created by manish.patwari on 5/15/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mMainActivity;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    Toolbar mToolbar;
    ImageButton navigationIcon;
    Comments mCommentView;
    ListView mCommentList;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        mToolbar = (Toolbar) mMainActivity.findViewById(R.id.tool_bar);
        mDrawerLayout = (DrawerLayout) mMainActivity.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) mMainActivity.findViewById(R.id.left_drawer);
    }

    public void test1Preconditions(){
        assertNotNull("ManinActivity is null" , mMainActivity);
        assertNotNull("ToolBar Obj is null" , mDrawerLayout);
        assertNotNull("DrawerLayout Obj is null" , mDrawerLayout);
        assertNotNull("DrawerList Obj is null" , mDrawerList);
    }

    public void test2DrawerDefaultClose(){
        assertFalse("Drawer is open by default", mDrawerLayout.isDrawerOpen(mDrawerList));
    }

    public void test3DrawerOpenAndCloseOnClickOfNavigationIcon() throws InterruptedException {
        for(int i = 0; i < mToolbar.getChildCount(); i++) {
            final View v = mToolbar.getChildAt(i);
            if (v instanceof ImageButton  && v.getContentDescription() == mMainActivity.getResources().getString(R.string.navigation_icon_desc)) {
                navigationIcon = ((ImageButton) v);
                break;
            }
        }
        assertNotNull("Navigation Icon Object is null", navigationIcon);

        assertTrue("Navigation Icon is not visible" ,navigationIcon.isShown());

        //Check for Opening Navigation Drawer.
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigationIcon.performClick();
            }
        });
        Thread.sleep(1000);
        assertTrue("Drawer is not opening on click of NaviagationIcon", mDrawerLayout.isDrawerOpen(mDrawerList));


        // Closing Navigation Drawer.
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigationIcon.performClick();
            }
        });
        Thread.sleep(1000);

        assertFalse("Drawer is not Closing on click of NaviagationIcon", mDrawerLayout.isDrawerOpen(mDrawerList));
    }

//    public void test4Comments() throws InterruptedException {
//
//        mCommentView = (Comments)mMainActivity.findViewById(R.id.comment);
//        assertNotNull("Comment container is not present", mCommentView);
//
//        mCommentList = (ListView) mCommentView.findViewById(R.id.comment_list);
//        assertNotNull("Comment List is not present", mCommentList);
//
//        CommentAdapter mCommentAdapter = (CommentAdapter) mCommentList.getAdapter();
//        assertNotNull("Comment Adapter is not present",mCommentAdapter);
//
//        assertEquals("Comment list is not null", 0, mCommentAdapter.getCount());
//
//       final EditText mCommentEditText = (EditText)mCommentView.findViewById(R.id.comment_text);
//
//        assertNotNull("Comment Edit Text is not present", mCommentEditText);
//
//        final Button mPostBtn = (Button) mCommentView.findViewById(R.id.comment_post_btn);
//        assertNotNull("Comment Post Button is not present", mPostBtn);
//
//
//        mMainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mPostBtn.performClick();
//            }
//        });
//        Thread.sleep(1000);
//        assertEquals("Empty Comment is post", 0, mCommentAdapter.getCount());
//
//
//        mMainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mCommentEditText.setText("Testing Comment");
//                mPostBtn.performClick();
//            }
//        });
//
//        Thread.sleep(1000);
//        assertEquals("Comment is not Posted", 1, mCommentAdapter.getCount());
//    }
}
