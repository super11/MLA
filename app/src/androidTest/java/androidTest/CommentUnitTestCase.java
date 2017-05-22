package androidTest;

import com.mla.newsapp.controller.CommentsCtrl;

import junit.framework.TestCase;

/**
 * Created by manish.patwari on 5/20/15.
 */
public class CommentUnitTestCase extends TestCase {
    CommentsCtrl mCommentCtrl;
    CommentsCtrl.Listeners mListeners;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mListeners = new CommentsCtrl.Listeners() {
            @Override
            public void dataUpdated() {

            }
        };
        mCommentCtrl = new CommentsCtrl(mListeners);
    }


    public void testCommentCtrl(){
        assertNotNull(mCommentCtrl);
        assertEquals(mCommentCtrl.getCommentItemsLength(),0);
        assertEquals(mCommentCtrl.getCommentAtPosition(0),null);
    }

    public void testCommentInsert(){
        mCommentCtrl.postComment("Testing1","Manish","TestingURL");
        assertEquals(mCommentCtrl.getCommentItemsLength(),1);
        assertEquals(mCommentCtrl.getCommentAtPosition(0).getCommentText(), "Testting1");


        mCommentCtrl.postComment("Testing2","Manish","TestingURL");
        assertEquals(mCommentCtrl.getCommentItemsLength(),2);
        assertEquals(mCommentCtrl.getCommentAtPosition(0).getCommentText(),"Testing2");

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
