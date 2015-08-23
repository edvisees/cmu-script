package edu.cmu.cs.lti.pipeline;

import edu.cmu.cs.lti.uima.io.reader.CustomCollectionReaderFactory;
import edu.cmu.cs.lti.uima.util.ProcessorManager;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 4/15/15
 * Time: 5:22 PM
 *
 * @author Zhengzhong Liu
 */
public class BasicPipeline {
    private CollectionReader reader;
    protected AnalysisEngineDescription[] preprocessors;
    protected AnalysisEngineDescription[] processors;
    protected AnalysisEngineDescription[] postprocessors;
    private AnalysisEngineDescription[] engines;

    private TypeSystemDescription typeSystemDescription;

    public BasicPipeline(AbstractProcessorBuilder builder, TypeSystemDescription typeSystemDescription) throws
            UIMAException {
        // Create the components
        reader = CollectionReaderFactory.createReader(builder.buildCollectionReader());
        preprocessors = builder.buildPreprocessors();
        processors = builder.buildProcessors();
        postprocessors = builder.buildPostProcessors();
        engines = ProcessorManager.joinProcessors(preprocessors, processors, postprocessors);
        this.typeSystemDescription = typeSystemDescription;
    }

    public void run() throws IOException, UIMAException {
        SimplePipeline.runPipeline(reader, engines);
    }

    public void run_processors(String outputParent, String outputBase) throws
            UIMAException, IOException {
        CollectionReaderDescription reader = CustomCollectionReaderFactory.createXmiReader(typeSystemDescription,
                outputParent,
                outputBase);
        SimplePipeline.runPipeline(reader, processors);
    }
}