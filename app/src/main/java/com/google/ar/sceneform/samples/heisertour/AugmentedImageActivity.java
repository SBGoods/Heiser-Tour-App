/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.sceneform.samples.heisertour;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.samples.common.helpers.SnackbarHelper;
import com.google.ar.sceneform.ux.ArFragment;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This application demonstrates using augmented images to place anchor nodes. app to include image
 * tracking functionality.
 */
public class AugmentedImageActivity extends AppCompatActivity {

  private ArFragment arFragment;
  private ImageView fitToScanView;
  private LinearLayout gallery;
  private Button mButton;
  private ActionBar mActionBar;
  private BottomNavigationView mBottomNavigationView;

  // Augmented image and its associated center pose anchor, keyed by the augmented image in
  // the database.
  private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
    fitToScanView = findViewById(R.id.image_view_fit_to_scan);

    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
    mButton = findViewById(R.id.Button);

    mBottomNavigationView = findViewById(R.id.navigationView);

    //initializeGallery();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (augmentedImageMap.isEmpty()) {
      fitToScanView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Registered with the Sceneform Scene object, this method is called at the start of each frame.
   *
   * @param frameTime - time since last frame.
   */
  private void onUpdateFrame(FrameTime frameTime) {
    Frame frame = arFragment.getArSceneView().getArFrame();

    // If there is no frame or ARCore is not tracking yet, just return.
    if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
      return;
    }

    Collection<AugmentedImage> updatedAugmentedImages =
        frame.getUpdatedTrackables(AugmentedImage.class);
    for (AugmentedImage augmentedImage : updatedAugmentedImages) {
      switch (augmentedImage.getTrackingState()) {
        case PAUSED:
          // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
          // but not yet tracked.
          String text = "Paused Detected Image " + augmentedImage.getIndex();
          SnackbarHelper.getInstance().showMessage(this, text);
          break;

        case TRACKING:
          // Have to switch to UI Thread to update View.
          fitToScanView.setVisibility(View.GONE);
          SnackbarHelper.getInstance().showMessage(this, "Tracking");

          // Create a new anchor for newly found images.
          if (!augmentedImageMap.containsKey(augmentedImage)) {
            SnackbarHelper.getInstance().showMessage(this, "Tracking " + augmentedImage.getName());
            Log.d("debug12", augmentedImage.getName());
            mButton.setVisibility(View.VISIBLE);
            AugmentedImageNode node = new AugmentedImageNode(this, augmentedImage.getName());
            node.setImage(augmentedImage);
            augmentedImageMap.put(augmentedImage, node);
            arFragment.getArSceneView().getScene().addChild(node);
            mButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
                intent.putExtra("NAME", augmentedImage.getName());
                startActivity(intent);

              }
            });
          }
          break;

        case STOPPED:
          SnackbarHelper.getInstance().showMessage(this, "Stopped");
          augmentedImageMap.remove(augmentedImage);
          fitToScanView.setVisibility(View.VISIBLE);
          mButton.setVisibility(View.INVISIBLE);
          break;
      }
    }
  }

  private void initializeGallery() {
    //gallery = findViewById(R.id.gallery_layout);

    ImageView Carson = new ImageView(this);
    Carson.setImageResource(R.drawable.carson1);
    Carson.setContentDescription("Carson");
    //lamp.setOnClickListener(view ->{buildObject("LampPost.sfb");});
    gallery.addView(Carson);

    ImageView Curie = new ImageView(this);
    Curie.setImageResource(R.drawable.curie1);
    Curie.setContentDescription("Curie");
    //couch.setOnClickListener(view ->{buildObject("couch.sfb");});
    gallery.addView(Curie);

    ImageView Earle = new ImageView(this);
    Earle.setImageResource(R.drawable.earle1);
    Earle.setContentDescription("Earle");
    //chair.setOnClickListener(view ->{buildObject("chair.sfb");});
    gallery.addView(Earle);

  }


}
