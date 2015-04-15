/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Contributors:
 * Artur Biesiadowski - abies@users.sourceforge.net
 */
package org.oss.pdfreporter.engine.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.oss.pdfreporter.exception.NotImplementedException;
import org.oss.pdfreporter.registry.IRegistry;
import org.oss.pdfreporter.repo.FileResourceLoader;
import org.oss.pdfreporter.uses.org.apache.digester.DelegatingAbstractDigester;
import org.oss.pdfreporter.uses.org.apache.digester.IDigester;
import org.oss.pdfreporter.xml.parsers.IInputSource;
import org.oss.pdfreporter.xml.parsers.XMLEntityResolver;
import org.oss.pdfreporter.xml.parsers.XMLParseException;



/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRXmlDigester.java 5180 2012-03-29 13:23:12Z teodord $
 */
public class JRXmlDigester extends DelegatingAbstractDigester implements XMLEntityResolver
{

	private final IDigester delegate;

	/**
	 *
	 */
	//private static boolean wasWarning;

	private Map<String,String> internalEntityResources;

	private String lastNamespacePrefix;
	private Object lastPopped;

	/**
	 *
	 */
	public JRXmlDigester(IDigester digester)
	{
		super(digester);
		this.delegate = digester;
		digester.setDelegator(this);

		initInternalResources();
		setXmlEntityResolver(this);
	}


	private void initInternalResources()
	{
		internalEntityResources = new HashMap<String,String>();

		internalEntityResources.put(JRXmlConstants.JASPERREPORT_SYSTEM_ID,
				JRXmlConstants.JASPERREPORT_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERPRINT_SYSTEM_ID,
				JRXmlConstants.JASPERPRINT_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERTEMPLATE_SYSTEM_ID,
				JRXmlConstants.JASPERTEMPLATE_DTD);
		internalEntityResources.put(JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID,
				JRXmlConstants.JASPERREPORT_XSD_RESOURCE);
		internalEntityResources.put(JRXmlConstants.JASPERPRINT_XSD_SYSTEM_ID,
				JRXmlConstants.JASPERPRINT_XSD_RESOURCE);
	}


	/**
	 * Adds a mapping of an entity system ID to an internal/classloader resource
	 * name.
	 *
	 * <p>
	 * This mapping is used by {@link #resolveEntity(String, String)} to
	 * resolve a system ID to a classloader resource.
	 *
	 * @param systemId the system ID
	 * @param resource the resource name
	 */
	public void addInternalEntityResource(String systemId, String resource)
	{
		internalEntityResources.put(systemId, resource);
	}


	/**
	 *
	 */
	public IInputSource resolveEntity(
		String pubId,
		String systemId
		)
	{
		IInputSource inputSource = null;

		if (systemId != null)
		{
			String resource = internalEntityResources.get(systemId);

			if (resource == null)
			{
				// TODO (25.04.2013, Donat, Open Software Solutions): Implement IInputSource with systemId
				throw new RuntimeException("Unknown entity resource with ID: " + systemId);
			}

			InputStream is = FileResourceLoader.getInputStream(resource);

			if (is != null)
			{
				inputSource = IRegistry.getIXmlParserFactory().newInputSource(is);
			}
		}

		return inputSource;
	}


	public void endElement(String namespaceURI, String localName, String qName)
			throws XMLParseException
	{
		lastNamespacePrefix = getNamespacePrefix(qName);

		delegate.endElement(namespaceURI, localName, qName);
	}

	protected String getNamespacePrefix(String qName)
	{
		String prefix;
		if (qName == null)
		{
			prefix = null;
		}
		else
		{
			int sepIdx = qName.indexOf(':');
			if (sepIdx > 0)
			{
				prefix = qName.substring(0, sepIdx);
			}
			else
			{
				prefix = null;
			}
		}
		return prefix;
	}

	public String getLastNamespacePrefix()
	{
		return lastNamespacePrefix;
	}


	@Override
	public Object pop()
	{
		// remember the last popped object
		lastPopped = delegate.pop();
		return lastPopped;
	}

	/**
	 * Clears the last popped object.
	 *
	 * @see #lastPopped()
	 */
	public void clearLastPopped()
	{
		lastPopped = null;
	}

	/**
	 * Returns the previously popped object.
	 *
	 * This method can be used by rules that need to know the object was added and
	 * popped to the stack by an inner element.
	 *
	 * @return the previously popped object
	 */
	public Object lastPopped()
	{
		return lastPopped;
	}


	@Override
	public void setDelegator(IDigester delegator) {
		throw new NotImplementedException("This implementation is not intended to offer this feature");

	}


	@Override
	public IDigester getDelegator() {
		throw new NotImplementedException("This implementation is not intended to offer this feature");
	}


}
