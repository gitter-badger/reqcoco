package net.paissad.tools.reqcoco.runner;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class ReqCoCoRunnerOptionsTest {

	private ReqCoCoRunnerOptions	options;

	private CmdLineParser			parser;

	@Rule
	public ExpectedException		thrown	= ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		this.options = new ReqCoCoRunnerOptions();
		options.parseOptions("--input", "/path/to/source", "--out", "/path/to/outputdir", "--source-code-path", "/path/to/sourcecode",
		        "--tests-code-path", "/path/to/testcode", "--log-level", "dummyLogLevel", "--includes", "*.txt,*.java", "--excludes", "*.bin",
		        "--ignores", "req_88,req_77", "arg1", "arg2");
		parser = new CmdLineParser(this);
	}

	@Test
	public void testParseOptionsWithHelp() throws CmdLineException {
		options.parseOptions("-h", "aaaaa");
		Assert.assertTrue(options.isHelp());
	}

	@Test
	public void testParseOptionsMissingRequiredSource() throws CmdLineException {
		thrown.expect(CmdLineException.class);
		options.parseOptions("--out", "/path/to/outputdir");
	}

	@Test
	public void testParseOptionsMissingRequiredOutput() throws CmdLineException {
		thrown.expect(CmdLineException.class);
		options.parseOptions("--input", "/path/to/source");
	}

	@Test
	public void testPrintUsage() {
		ReqCoCoRunnerOptions.printUsage(parser);
	}

	@Test
	public void testIsHelp() {
		Assert.assertFalse(options.isHelp());
	}

	@Test
	public void testGetOutputFolder() {
		Assert.assertEquals("/path/to/outputdir", options.getOutputFolder());
	}

	@Test
	public void testGetRequirementSource() {
		Assert.assertEquals("/path/to/source", options.getRequirementSource());
	}

	@Test
	public void testGetSourceCodePath() {
		Assert.assertEquals("/path/to/sourcecode", options.getSourceCodePath());
	}

	@Test
	public void testGetTestsCodePath() {
		Assert.assertEquals("/path/to/testcode", options.getTestsCodePath());
	}

	@Test
	public void testGetFilesInclude() {
		Assert.assertEquals("*.txt,*.java", options.getFilesInclude());
	}

	@Test
	public void testGetFilesExclude() {
		Assert.assertEquals("*.bin", options.getFilesExclude());
	}

	@Test
	public void testGetIgnores() {
		Assert.assertEquals("req_88,req_77", options.getIgnores());
	}

	@Test
	public void testGetBuildConsoleReport() {
		Assert.assertFalse(Boolean.valueOf(options.getBuildConsoleReport()));
	}

	@Test
	public void testGetBuildHtmlReport() {
		Assert.assertTrue("Default value should be 'true'", Boolean.valueOf(options.getBuildHtmlReport()));
	}

	@Test
	public void testGetReportName() {
		Assert.assertNull(options.getReportName());
	}

	@Test
	public void testGetLogLevel() {
		Assert.assertEquals("dummyLogLevel", options.getLogLevel());
	}

	@Test
	public void testGetArguments() {
		Assert.assertEquals(Arrays.asList(new String[] { "arg1", "arg2" }), options.getArguments());
	}

}
