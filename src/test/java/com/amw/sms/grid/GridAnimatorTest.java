package com.amw.sms.grid;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.amw.sms.algorithms.MazeAlgorithmExecutionState;
import com.amw.sms.algorithms.ObservableMazeAlgorithm;
import com.amw.sms.grid.griddata.GridData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ij.ImagePlus;
import ij.process.ByteProcessor;

@ExtendWith(MockitoExtension.class)
public class GridAnimatorTest {
    @Mock
    private Grid mockGrid;

    @Mock
    private ObservableMazeAlgorithm mockAlgorithm;

    @Mock
    private MazeAlgorithmExecutionState mockExecutionState;

    @Mock private GridData mockGridData1;
    @Mock private GridData mockGridData2;

    @Mock 
    private List<Cell> mockPath;

    @TempDir
    private File tempDirectory;

    //Animator under test
    private GridAnimator gridAnimator;

    private MazeAlgorithmExecutionState sampleExecutionState;
    private ImagePlus sampleImage1;
    private ImagePlus sampleImage2;
    private String samplePath1;
    private String samplePath2;

    @BeforeEach
    void beforeEach(){
        gridAnimator = new GridAnimator(mockGrid, mockAlgorithm);
        sampleExecutionState = new MazeAlgorithmExecutionState();
        sampleImage1 = new ImagePlus("sample1", new ByteProcessor(1, 1, new byte[]{0}));
        sampleImage2 = new ImagePlus("sample2", new ByteProcessor(1, 1, new byte[]{127}));

        //Always-mocked methods         
        Mockito.lenient()
            .when(mockGrid.getGridData())
            .thenReturn(mockGridData1);

        samplePath1 = tempDirectory.getAbsolutePath() + "/GridAnimatorTest_saveToFile_success.gif";
        samplePath2 = tempDirectory.getAbsolutePath() + "/GridAnimatorTest_saveToFile_failure.gif";
    }

    /**
     * Helper. Stubs mockGrid toImage methods to return provided image.
     * @param image
     */
    private void stubToImage(ImagePlus image){
        //Cover both relevant methods with leniency since only a few tests care about which one is actually to be called.
        Mockito
            .lenient()
            .when(mockGrid.toImage(any(), anyInt(), any()))
            .thenReturn(sampleImage1);

        Mockito
            .lenient()
            .when(mockGrid.toImage(any(), anyInt()))
            .thenReturn(sampleImage1);
    }

    /**
     * Helper. Stubs mockAglorithm getExecutionState to return provided execution state.
     * @param executionState
     */
    private void stubGetExecutionState(MazeAlgorithmExecutionState executionState){
        Mockito.lenient()
            .when(mockAlgorithm.getExecutionState())
            .thenReturn(executionState);
    }

    @Test
    void testRecord_startsObservingTheAlgorithm(){
        Mockito.verify(mockAlgorithm, times(0))
            .attach(gridAnimator);
        
        gridAnimator.record();

        Mockito.verify(mockAlgorithm, times(1))
            .attach(gridAnimator);
    }

    @Test
    void testAlgorithmFinished_andRecord_stopsObservingTheAlgorithm(){
        this.stubGetExecutionState(sampleExecutionState);
        this.stubToImage(sampleImage1);

        gridAnimator.record();
        gridAnimator.algorithmFinished();

        Mockito.verify(mockAlgorithm, times(1))
            .detach(gridAnimator);
    }

    @Test
    void testAlgorithmFinished_andRecord_andGetAnimation_whenNoFramesHaveBeenRecorded_animationIsSingleFrameOfGrid(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        gridAnimator.record();
        gridAnimator.algorithmFinished();

        final var animationFrames = gridAnimator
            .getAnimation()
            .get()
            .getStack();

        assertEquals(1, animationFrames.getSize());
        assertEquals(sampleImage1.getStack().getPixels(1), animationFrames.getPixels(1));

        Mockito.verify(mockGrid, times(1))
            .toImage(any(), anyInt());
    }

    @Test
    void testAlgorithmFinished_andRecord_whenNoAlgorithmGridDataIsSet_frameOfGridIsRecordedNotUsingAnySeparateGridData(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        Mockito.when(mockExecutionState.getAlgorithmGridData())
            .thenReturn(Optional.empty());

        gridAnimator.record();
        gridAnimator.algorithmFinished();

        Mockito.verify(mockGrid, times(1))
            .toImage(any(), anyInt());
    }

    @Test
    void testAlgorithmFinished_andRecord_whenAlgorithmGridDataIsSet_frameOfGridIsRecordedUsingAlgorithmGridData(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        Mockito.when(mockExecutionState.getAlgorithmGridData())
            .thenReturn(Optional.of(mockGridData2));

        gridAnimator.record();
        gridAnimator.algorithmFinished();

        Mockito.verify(mockGrid, times(1))
            .toImage(any(), anyInt(), eq(mockGridData2));
    }

    @Test
    void testEndRecording_andRecord_stopsObservingTheAlgorithm(){
        this.stubGetExecutionState(sampleExecutionState);
        this.stubToImage(sampleImage1);

        gridAnimator.record();
        gridAnimator.endRecording();

        Mockito.verify(mockAlgorithm, times(1))
            .detach(gridAnimator);
    }

    @Test
    void testEndRecording_andRecord_andGetAnimation_whenNoFramesHaveBeenRecorded_animationIsSingleFrameOfGrid(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        gridAnimator.record();
        gridAnimator.endRecording();

        final var animationFrames = gridAnimator
            .getAnimation()
            .get()
            .getStack();

        assertEquals(1, animationFrames.getSize());
        assertEquals(sampleImage1.getStack().getPixels(1), animationFrames.getPixels(1));

        Mockito.verify(mockGrid, times(1))
            .toImage(any(), anyInt());
    }

    @Test
    void testEndRecording_andRecord_whenNoAlgorithmGridDataIsSet_frameOfGridIsRecordedNotUsingAnySeparateGridData(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        Mockito.when(mockExecutionState.getAlgorithmGridData())
            .thenReturn(Optional.empty());

        gridAnimator.record();
        gridAnimator.endRecording();

        Mockito.verify(mockGrid, times(1))
            .toImage(any(), anyInt());
    }

    @Test
    void testEndRecording_andRecord_whenAlgorithmGridDataIsSet_frameOfGridIsRecordedUsingAlgorithmGridData(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        Mockito.when(mockExecutionState.getAlgorithmGridData())
            .thenReturn(Optional.of(mockGridData2));

        gridAnimator.record();
        gridAnimator.endRecording();

        Mockito.verify(mockGrid, times(1))
            .toImage(any(), anyInt(), eq(mockGridData2));
    }

    @Test
    void testAlgorithmStarted_andRecord_andEndRecording_addsFrameToAnimation(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        gridAnimator.record();
        gridAnimator.algorithmStarted();
        gridAnimator.endRecording();

        final var animationFrames = gridAnimator
            .getAnimation()
            .get()
            .getStack();

        assertEquals(1, animationFrames.getSize());
        assertEquals(sampleImage1.getStack().getPixels(1), animationFrames.getPixels(1));

        Mockito.verify(mockGrid, times(1))
            .toImage(any(), anyInt());
    }

    @Test
    void testAlgorithmCompletedStep_andOtherMethods_createsAnimationOfThreeFramesAsExpected(){
        this.stubGetExecutionState(mockExecutionState);

        Mockito
            .when(mockGrid.toImage(any(), anyInt()))
            .thenReturn(sampleImage1)   //First frame
            .thenReturn(sampleImage2)   //Second frame
            .thenReturn(sampleImage1);  //Third frame

        gridAnimator.record();                  //Starts recording
        gridAnimator.algorithmStarted();        //First frame
        gridAnimator.algorithmCompletedStep();  //Second frame
        gridAnimator.algorithmCompletedStep();  //Third frame
        gridAnimator.algorithmFinished();       //Animation completed

        final var animationFrames = gridAnimator
            .getAnimation()
            .get()
            .getStack();

        assertEquals(3, animationFrames.getSize());
        assertEquals(sampleImage1.getStack().getPixels(1), animationFrames.getPixels(1));
        assertEquals(sampleImage2.getStack().getPixels(1), animationFrames.getPixels(2));
        assertEquals(sampleImage1.getStack().getPixels(1), animationFrames.getPixels(3));


        Mockito.verify(mockGrid, times(3))
            .toImage(any(), anyInt());
    }

    @Test 
    void testGetAnimation_whenNothingIsRecorded_returnsEmptyOptional(){
        assertTrue(gridAnimator.getAnimation().isEmpty());
    }

    @Test
    void testGetAnimation_whenRecordingStartedButNotFinished_returnsEmptyOptional(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage1);

        gridAnimator.record();
        gridAnimator.algorithmStarted();
        gridAnimator.algorithmCompletedStep();

        assertTrue(gridAnimator.getAnimation().isEmpty());
    }

    @Test
    void testSaveToFile_whenAnimationCompleted_imageIsSavedToPathAsExpected(){
        this.stubGetExecutionState(mockExecutionState);
        this.stubToImage(sampleImage2);

        gridAnimator.record();
        gridAnimator.algorithmStarted();
        gridAnimator.algorithmCompletedStep();
        gridAnimator.algorithmFinished();

        gridAnimator.saveToFile(samplePath1);

        final var file = new File(samplePath1);
        assertTrue(file.exists());

        final var actualImage = new ImagePlus(file.getAbsolutePath());
        final var actualFrames = actualImage.getStack();
        assertEquals(2, actualFrames.getSize());

        //Can't find easy way to compare frames of saved image. 
        //TODO
    }

    @Test
    void testSaveToFile_whenAnimationIsNotCompleted_noImageIsSavedToPath(){
        gridAnimator.saveToFile(samplePath2);

        final var file = new File(samplePath2);
        assertFalse(file.exists());
    }

    @Test
    void testGetGrid(){
        assertEquals(mockGrid, gridAnimator.getGrid());
    }

    @Test
    void testGetObservedAlgorithm(){
        assertEquals(mockAlgorithm, gridAnimator.getObservedAlgorithm());
    }
}
