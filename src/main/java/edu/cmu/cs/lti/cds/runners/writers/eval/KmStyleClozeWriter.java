/**
 *
 */
package edu.cmu.cs.lti.cds.runners.writers.eval;

import edu.cmu.cs.lti.cds.annotators.writers.eval.KmStyleAllEventMentionClozeTaskGenerator;
import edu.cmu.cs.lti.uima.io.reader.CustomCollectionReaderFactory;
import edu.cmu.cs.lti.uima.io.writer.CustomAnalysisEngineFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import java.io.IOException;

/**
 * @author zhengzhongliu
 */
public class KmStyleClozeWriter {
    private static String className = KmStyleClozeWriter.class.getSimpleName();

    /**
     * @param args
     * @throws java.io.IOException
     * @throws org.apache.uima.UIMAException
     */
    public static void main(String[] args) throws UIMAException, IOException {
        System.out.println(className + " started...");

        // ///////////////////////// Parameter Setting ////////////////////////////
        // Note that you should change the parameters below for your configuration.
        // //////////////////////////////////////////////////////////////////////////
        // Parameters for the reader
        String paramInputDir = "data/02_event_tuples";
        int stepNum = 3;

        // Parameters for the writer
        String paramParentOutputDir = "data";
        String paramBaseOutputDirName = "cloze_files";
        String paramOutputFileSuffix = ".txt";
        // ////////////////////////////////////////////////////////////////

        String paramTypeSystemDescriptor = "TypeSystem";

        // Instantiate the analysis engine.
        TypeSystemDescription typeSystemDescription = TypeSystemDescriptionFactory
                .createTypeSystemDescription(paramTypeSystemDescriptor);

        // Instantiate a collection reader to get XMI as input.
        // Note that you should change the following parameters for your setting.
        CollectionReaderDescription reader =
                CustomCollectionReaderFactory.createTimeSortedGzipXmiReader(typeSystemDescription, paramInputDir, false);

        AnalysisEngineDescription writer = CustomAnalysisEngineFactory.createAnalysisEngine(
                KmStyleAllEventMentionClozeTaskGenerator.class, typeSystemDescription,
                KmStyleAllEventMentionClozeTaskGenerator.PARAM_BASE_OUTPUT_DIR_NAME, paramBaseOutputDirName,
                KmStyleAllEventMentionClozeTaskGenerator.PARAM_OUTPUT_FILE_SUFFIX, paramOutputFileSuffix,
                KmStyleAllEventMentionClozeTaskGenerator.PARAM_PARENT_OUTPUT_DIR, paramParentOutputDir,
                KmStyleAllEventMentionClozeTaskGenerator.PARAM_STEP_NUMBER, stepNum);

        SimplePipeline.runPipeline(reader, writer);

        System.out.println(className + " completed.");
    }
}