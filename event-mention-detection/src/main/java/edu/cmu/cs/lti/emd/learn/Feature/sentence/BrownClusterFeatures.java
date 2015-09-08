package edu.cmu.cs.lti.emd.learn.feature.sentence;

import edu.cmu.cs.lti.script.type.StanfordCorenlpSentence;
import edu.cmu.cs.lti.script.type.StanfordCorenlpToken;
import edu.cmu.cs.lti.utils.Configuration;
import gnu.trove.map.TObjectDoubleMap;
import org.apache.commons.io.FileUtils;
import org.apache.uima.jcas.JCas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Date: 9/6/15
 * Time: 9:09 PM
 *
 * @author Zhengzhong Liu
 */
public class BrownClusterFeatures extends SentenceFeatureWithFocus {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private int[] brownClusterPrefix = {13, 16, 20};

//    private ArrayListMultimap<String, String> brownClusters;

    private Map<String, String> brownClusters;

    private String brownClusteringPath;

    public BrownClusterFeatures(Configuration config) {
        super(config);
        brownClusteringPath = config.get("edu.cmu.cs.lti.brown_cluster.path");
        logger.info("Loading Brown clusters");
        brownClusters = new HashMap<>();
        try {
            for (String line : FileUtils.readLines(new File(brownClusteringPath))) {
                String[] parts = line.split("\t");
                if (parts.length > 2) {
                    brownClusters.put(parts[1], parts[0]);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void initWorkspace(JCas context) {
    }

    @Override
    public void resetWorkspace(StanfordCorenlpSentence sentence) {

    }

    @Override
    public void extract(List<StanfordCorenlpToken> sentence, int focus, TObjectDoubleMap<String> features,
                        TObjectDoubleMap<String> featuresNeedForState) {
        String lemma = sentence.get(focus).getLemma();
        if (brownClusters.containsKey(lemma)) {
            String fullClusterId = brownClusters.get(lemma);
            for (int prefixLength : brownClusterPrefix) {
                if (prefixLength <= fullClusterId.length()) {
                    String brownClusterLabel = fullClusterId.substring(0, prefixLength);
                    features.put((String.format("HeadLemmaBrown@%d=%s", prefixLength, brownClusterLabel)), 1);
                }
            }
            features.put((String.format("HeadLemmaBrownFull=%s", fullClusterId)), 1);
        }
    }
}
