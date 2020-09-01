package com.yocn.meida.view.activity;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.yocn.media.R;
import com.yocn.meida.camera.Camera2ProviderWithGL;
import com.yocn.meida.gles.GlUtil;
import com.yocn.meida.gles.render.SquarePreviewCameraRender;

/**
 * @Author yocn
 * @Date 2019/8/4 9:46 AM
 * @ClassName PreviewDataActivity
 * 预览并获取数据
 */
public class PreviewWithOpenGLESActivity extends BaseCameraActivity {
    GLSurfaceView mPreviewGlSurafceView;
    Camera2ProviderWithGL mCamera2Provider;
    public static String DESC = "Camera2 通过OpenGLES预览";
    TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().inflate(R.layout.activity_preview_opengles, null);
        setContentView(rootView);
        initView(rootView);
        initData();
    }

    @Override
    protected void initView(View root) {
        super.initView(root);
        mPreviewGlSurafceView = root.findViewById(R.id.glsv_preview);
        textureView = root.findViewById(R.id.tv_camera);
        mPreviewGlSurafceView.setEGLContextClientVersion(2);
    }

    @Override
    protected void initData() {
        mCamera2Provider = new Camera2ProviderWithGL(this);
        int mTextureId = GlUtil.getOESTextureId();
        SurfaceTexture mSurfaceTexture = new SurfaceTexture(mTextureId);
        mCamera2Provider.initTexture(mSurfaceTexture, textureView);
        mSurfaceTexture.setOnFrameAvailableListener(surfaceTexture -> {
            Log.d(TAG, "onFrameAvailable: ");
            mPreviewGlSurafceView.requestRender();
        });
        SquarePreviewCameraRender squarePreviewCameraRender = new SquarePreviewCameraRender(mSurfaceTexture, mTextureId);
        mPreviewGlSurafceView.setRenderer(squarePreviewCameraRender);
    }

    @Override
    protected void onDestroy() {
        mCamera2Provider.closeCamera();
        super.onDestroy();
    }
}