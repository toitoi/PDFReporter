package org.oss.pdfreporter.xml.parsers.wrapper;

import org.oss.pdfreporter.uses.org.w3c.dom.Attr;
import org.oss.pdfreporter.uses.org.w3c.dom.DOMException;
import org.oss.pdfreporter.uses.org.w3c.dom.Element;
import org.oss.pdfreporter.uses.org.w3c.dom.TypeInfo;
import org.oss.pdfreporter.xml.parsers.util.XmlParserUnmarshaller;


public class DelegatingAttr extends DelegatingNode implements Attr {
	private final org.w3c.dom.Attr delegate;

	public DelegatingAttr(org.w3c.dom.Attr delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	public org.w3c.dom.Attr getDelegate() {
		return delegate;
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public boolean getSpecified() {
		return delegate.getSpecified();
	}

	@Override
	public String getValue() {
		return delegate.getValue();
	}

	@Override
	public void setValue(String value) throws DOMException {
		delegate.setValue(value);
	}

	@Override
	public Element getOwnerElement() {
		return null;
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		return XmlParserUnmarshaller.getTypeInfo(delegate.getSchemaTypeInfo());
	}

	@Override
	public boolean isId() {
		return delegate.isId();
	}

	
	
}
