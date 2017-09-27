package at.ac.acdh.concept_mapping;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * <p>
 * Link class connects
 * <a href="https://www.clarin.eu/content/component-metadata">CLARIN CMDI</a>
 * elements with CIDOC-CRM ontology. It uses same elements as
 * <a href="https://www.clarin.eu/">CLARIN</a> mapping between concepts and
 * facets such as patterns, concepts and blacklistPatterns
 * </p>
 * 
 * 
 * @author dostojic
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Link extends Node {

	@XmlElement(name = "entity")
	Collection<ParthenosEntity> entities = new ArrayList<>();

	@XmlElement(name = "concept")
	Collection<String> concepts = new ArrayList<>();

	@XmlElement(name = "pattern")
	Collection<String> patterns = new ArrayList<>();

	@XmlElement(name = "blacklistPattern")
	Collection<String> blacklistPatterns = new ArrayList<>();

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("link:\n");

		for (ParthenosEntity pe : entities)
			sb.append(pe.toString());

		for (String concept : concepts)
			sb.append(concept + "\n");

		for (String pattern : patterns)
			sb.append(pattern + "\n");

		return sb.toString();
	}

	/**
	 * <p>
	 * In case of multiple entities, intermediate nodes are created. In that
	 * case original order is preserved.
	 * </p>
	 * 
	 * @return list of {@link ParthenosEntity}
	 * 
	 */
	public Collection<ParthenosEntity> getEntities() {
		return entities;
	}

	public void setEntities(Collection<ParthenosEntity> entities) {
		this.entities = entities;
	}

	/**
	 * @return list of specified CLARIN concepts
	 * 
	 *         <p>
	 * 		Concepts will be "translated" to xpaths for each profile. One
	 *         concept can be "translated" to many xpaths.
	 *         </p>
	 * 
	 */
	public Collection<String> getConcepts() {
		return concepts;
	}

	public void setConcepts(Collection<String> concepts) {
		this.concepts = concepts;
	}

	/**
	 * <p>
	 * For each pattern one link will be created.
	 * </p>
	 * 
	 * @return list of specified xpaths in CLARIN profiles
	 * 
	 * @see gr.forth.x3ml.SourceRelationType
	 *      gr.forth.x3ml.X3ML.Mappings.Mapping.Link.Range
	 * 
	 */
	public Collection<String> getPatterns() {
		return patterns;
	}

	public void setPatterns(Collection<String> patterns) {
		this.patterns = patterns;
	}

	/**
	 * <p>
	 * Patterns derived from concepts, that are specified in blacklist will be
	 * removed from further translation into x3ml classes
	 * </p>
	 * 
	 * @return blacklist of xpaths
	 * 
	 */
	public Collection<String> getBlacklistPatterns() {
		return blacklistPatterns;
	}

	public void setBlacklistPatterns(Collection<String> blacklistPatterns) {
		this.blacklistPatterns = blacklistPatterns;
	}

}