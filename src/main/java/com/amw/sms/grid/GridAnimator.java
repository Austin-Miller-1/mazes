package com.amw.sms.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.amw.sms.algorithms.ObservableMazeAlgorithm;
import com.amw.sms.algorithms.solving.OneTimeMazeAlgorithmObserver;
import com.amw.sms.grid.griddata.GridData;

import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.GifWriter;

/**
 * Algorithm observer that animates a grid as it is put under an observable
 * algorithm. This algorithm may be a generation algorithm or a solving algorithm.
 * The expectation is that the animator animates a grid under a singular algorithm.
 * Once the algorithm is done, the animation can be retrieved or saved. 
 */
public class GridAnimator extends OneTimeMazeAlgorithmObserver {
    private static final String ANIMATION_TITLE = "GridAnimation";
    private static final String ANIMATION_FRAME_TITLE = "GridAnimationFrame";
    private static final int ANIMATION_GRID_CELL_SIZE = 30;
    private final Grid grid;
    private final GridData algorithmGridData;
    private List<ImagePlus> frames;
    private Optional<ImagePlus> animation;

    /**
     * Constructs new GridAnimator.
     * @param grid The grid to create the animation for.
     * @param observedAlgorithm The algorithm to create the animation of.
     * @return New grid-animator instance.
     */
    public GridAnimator(Grid grid, ObservableMazeAlgorithm observedAlgorithm){
        super(observedAlgorithm);
        this.grid = grid;
        this.algorithmGridData = new GridData(grid);
        this.frames = new ArrayList<ImagePlus>();
        this.animation = Optional.empty();
    }

    /**
     * {@inheritDoc}
     * Records first frame of animation.
     */
    @Override
    public void algorithmStarted() {
        recordNextFrame();
    }

    /**
     * {@inheritDoc}
     * Records next frame of animation.
     */
    @Override
    public void algorithmCompletedStep() {
        recordNextFrame();
    }

    /**
     * {@inheritDoc}
     * Finishes the animation recording by stacking all of the individual frames and placing
     * them collectively into a new animated-image object. Once invoked, this animation will
     * can be accessed via other GridAnimator methods. 
     * If no frames have been recorded, an image of the current grid with the algorithm's 
     * execution state will be used as the resulting image.
     * @see GridAnimator#saveToFile(String)
     * @see GridAnimator#getAnimation()
     */
    @Override
    public void onAlgorithmFinish() {
        //Case where no frames were recorded during algorithm's execution
        //Take current frame of grid and save it.
        if(this.frames.isEmpty()){
            this.recordNextFrame();
        }

        final var imageStack = ImageStack.create(this.frames.toArray(new ImagePlus[0]));
        this.animation = Optional.of(new ImagePlus(ANIMATION_TITLE, imageStack));
    }
    
    /**
     * Starts recording the grid and algorithm. This is done by observing the algorithm
     * and its execution state as it is executed.
     */
    public void record(){
        this.observedAlgorithm.attach(this);
    }

    /**
     * Force-stops the recording of the grid and the algorithm. Invoking this method will
     * cause the animator to act exactly as it would have if the algorithm had actually been executed
     * to completion. That is, when called, the animator no-longer observes the algorithm and
     * an animation of the currently-recorded frames will become available to any clients.
     * @see GridAnimator#onAlgorithmFinish()
     */
    public void endRecording(){
        this.algorithmFinished();
    }

    /**
     * Saves the next frame of the grid's animation. If the algorithm's execution state
     * contains a GridData instance, this data will be used when retrieving the image of the
     * grid. If no such grid-data is set, then an image of the grid will be retrieved without
     * it, i.e. the an image of the grid with whatever data it already has set will be used.
     */
    private void recordNextFrame(){
        final var algorithmGridData = this.observedAlgorithm
            .getExecutionState()
            .getAlgorithmGridData();

        final var nextFrame = algorithmGridData.isPresent()
            ?   this.grid.toImage(ANIMATION_FRAME_TITLE, ANIMATION_GRID_CELL_SIZE, algorithmGridData.get())
            :   this.grid.toImage(ANIMATION_FRAME_TITLE, ANIMATION_GRID_CELL_SIZE);

        this.frames.add(nextFrame);        
    }

    /**
     * Returns optional containing the completed animation.
     * @return Optional containing the completed animation. If the algorithm has not completed
     * OR the endRecording method has not been called, this Optional will be empty.
     */
    public Optional<ImagePlus> getAnimation(){
        return this.animation;
    }

    /**
     * Saves the animation (if one exists) to the provided path. This will be saved as an
     * animated GIF image. Path should end with the ".gif" file extension so that the image
     * can be opened properly by whatever computer will be trying to open it.
     * @param path Path to save the image file to.
     */
    public void saveToFile(String path){
        if(this.animation.isPresent()){
            GifWriter.save(this.animation.get(), path);
        }
    }

   
    /**
     * Get the grid this animator is for.
     * @return Grid.
     */
    Grid getGrid(){
        return this.grid;
    }

    /**
     * Get the algorithm that this animator is watching.
     * @return
     */
    ObservableMazeAlgorithm getObservedAlgorithm(){
        return this.observedAlgorithm;
    }
}
