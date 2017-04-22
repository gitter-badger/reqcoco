package net.paissad.tools.reqcoco.api.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "revision")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class Revision implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@XmlAttribute(required = true)
	private String				value;

	public Revision() {
		// default no-arg constructor
	}

}
