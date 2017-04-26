package net.paissad.tools.reqcoco.generator.simple.impl;

import java.net.URI;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.paissad.tools.reqcoco.generator.simple.api.ReqSourceParser;
import net.paissad.tools.reqcoco.generator.simple.impl.tag.SimpleReqTagSourceConfig;

public class AbstractReqGeneratorConfigTest {

	private AbstractReqGeneratorConfig config;

	@Before
	public void setUp() throws Exception {
		config = new AbstractReqGeneratorConfig() {

			@Override
			public Path getTestsCodePath() {
				return null;
			}

			@Override
			public URI getSourceRequirements() {
				return null;
			}

			@Override
			public ReqSourceParser getSourceParser() {
				return null;
			}

			@Override
			public Path getSourceCodePath() {
				return null;
			}

			@Override
			public Path getCoverageOutput() {
				return null;
			}
		};
	}

	@Test
	public void testGetSourceCodeTagConfig() {
		Assert.assertNotNull(this.config.getSourceCodeTagConfig());
		Assert.assertTrue(this.config.getSourceCodeTagConfig() instanceof SimpleReqTagSourceConfig);
	}

	@Test
	public void testGetTestsTagConfig() {
		Assert.assertNotNull(this.config.getTestsCodeTagConfig());
		Assert.assertTrue(this.config.getSourceCodeTagConfig() instanceof SimpleReqTagSourceConfig);
	}

	@Test
	public void testGetIgnoreList() {
		Assert.assertNotNull(this.config.getIgnoreList());
		Assert.assertTrue(this.config.getIgnoreList().isEmpty());
	}

	@Test
	public void testGetFileIncludes() {
		Assert.assertNotNull(this.config.getFileExcludes());
		Assert.assertTrue(this.config.getFileIncludes().isEmpty());
	}

	@Test
	public void testGetFileExcludes() {
		Assert.assertNotNull(this.config.getFileExcludes());
		Assert.assertTrue(this.config.getFileExcludes().isEmpty());
	}

	@Test
	public void testGetExtraOptions() {
		Assert.assertNotNull(this.config.getExtraOptions());
		Assert.assertTrue(this.config.getExtraOptions().isEmpty());
	}

}
