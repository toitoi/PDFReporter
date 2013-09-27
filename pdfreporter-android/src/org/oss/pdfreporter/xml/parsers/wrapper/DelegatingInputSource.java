package org.oss.pdfreporter.xml.parsers.wrapper;

import java.io.InputStream;
import java.io.Reader;

import org.oss.pdfreporter.xml.parsers.IInputSource;
import org.xml.sax.InputSource;


public class DelegatingInputSource implements IInputSource{
	private final InputSource delegate;

	public DelegatingInputSource(InputSource delegate) {
		super();
		this.delegate = delegate;
	}

	public InputSource getDelegate() {
		return delegate;
	}

	@Override
	public void setByteStream(InputStream byteStream) {
		delegate.setByteStream(byteStream);
	}

	@Override
	public InputStream getByteStream() {
		return delegate.getByteStream();
	}

	@Override
	public void setCharacterStream(Reader characterStream) {
		delegate.setCharacterStream(characterStream);
	}

	@Override
	public Reader getCharacterStream() {
		return delegate.getCharacterStream();
	}
	
	
}
