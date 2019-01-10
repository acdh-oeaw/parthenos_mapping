package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;

/*
* @author Wolfgang Walter SAUER (wowasa) &lt;wolfgang.sauer@oeaw.ac.at&gt;
* 
* This extension of the LabelGenerator is necessary to create specific Arg objects which allow the use of concepts, blacklist patterns and default patterns to set schema 
* specific xpaths. The same approach is used in the Link object already. 
*/
public class LabelGenerator extends gr.forth.x3ml.LabelGenerator{
    
    public static class Arg extends gr.forth.x3ml.Arg{
        @XmlElement(name = "concept")
        protected Collection<String> concepts = new ArrayList<>();

        @XmlElement(name = "pattern")
        protected Collection<String> patterns = new ArrayList<>();

        @XmlElement(name = "blacklistPattern")
        protected Collection<String> blacklistPatterns = new ArrayList<>();

        public Collection<String> getConcepts() {
            return concepts;
        }

        public void setConcepts(Collection<String> concepts) {
            this.concepts = concepts;
        }

        public Collection<String> getPatterns() {
            return patterns;
        }

        public void setPatterns(Collection<String> patterns) {
            this.patterns = patterns;
        }

        public Collection<String> getBlacklistPatterns() {
            return blacklistPatterns;
        }

        public void setBlacklistPatterns(Collection<String> blacklistPatterns) {
            this.blacklistPatterns = blacklistPatterns;
        }        
    }
}
