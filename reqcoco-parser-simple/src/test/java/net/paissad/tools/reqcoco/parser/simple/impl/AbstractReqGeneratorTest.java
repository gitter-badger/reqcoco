package net.paissad.tools.reqcoco.parser.simple.impl;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.paissad.tools.reqcoco.api.model.Requirement;
import net.paissad.tools.reqcoco.api.model.Requirements;
import net.paissad.tools.reqcoco.parser.simple.api.ReqGeneratorConfig;
import net.paissad.tools.reqcoco.parser.simple.api.ReqSourceParser;
import net.paissad.tools.reqcoco.parser.simple.exception.ReqGeneratorConfigException;
import net.paissad.tools.reqcoco.parser.simple.exception.ReqGeneratorExecutionException;
import net.paissad.tools.reqcoco.parser.simple.exception.ReqSourceParserException;
import net.paissad.tools.reqcoco.parser.simple.impl.AbstractReqGenerator;
import net.paissad.tools.reqcoco.parser.simple.impl.AbstractReqGeneratorConfig;

public class AbstractReqGeneratorTest {

	private AbstractReqGenerator	reqGenerator;

	private ReqGeneratorConfig		reqGeneratorConfigStub;

	private Path					coverageOutputReportFile;

	@Rule
	public ExpectedException		thrown	= ExpectedException.none();

	private URI						declarationSource;

	private Path					sourcePath;

	private Path					testsPath;

	@Before
	public void setUp() throws Exception {
		this.reqGenerator = new AbstractReqGenerator() {
		};
		this.declarationSource = AbstractReqGeneratorTest.class.getResource("/samples/input/req_declarations_1.txt").toURI();
		this.sourcePath = Paths.get(AbstractReqGeneratorTest.class.getResource("/samples/input/code/source").toURI());
		this.testsPath = Paths.get(AbstractReqGeneratorTest.class.getResource("/samples/input/code/test").toURI());
		this.reqGeneratorConfigStub = this.getConfigStub();
		this.coverageOutputReportFile = Files.createTempFile(getClass().getSimpleName(), "--req-coverage-report.xml");
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteQuietly(this.coverageOutputReportFile.toFile());
	}

	@Test
	public void testConfigure() throws ReqGeneratorConfigException {

		this.reqGenerator.configure(this.reqGeneratorConfigStub);

		Assert.assertEquals(this.reqGeneratorConfigStub, this.reqGenerator.getConfig());
		Assert.assertTrue(this.reqGeneratorConfigStub == this.reqGenerator.getConfig());
	}

	@Test
	public void testRun() throws ReqGeneratorConfigException, ReqGeneratorExecutionException {

		this.reqGenerator.configure(this.reqGeneratorConfigStub);
		this.reqGenerator.run();
	}

	@Test
	public void testRunWithIgnore() throws ReqGeneratorConfigException, ReqGeneratorExecutionException {

		this.reqGeneratorConfigStub.getIgnoreList().add("req_1");
		this.reqGenerator.configure(this.reqGeneratorConfigStub);
		final Collection<Requirement> computedRequirements = this.reqGenerator.run();
		final Collection<Requirement> reqs1 = Requirements.getById(computedRequirements, "req_1");
		Assert.assertEquals(1, reqs1.size());
		Assert.assertTrue(reqs1.iterator().next().isIgnore());
	}

	@Test
	public void testRunWhenSourcePathIsUnexistent() throws ReqGeneratorConfigException, ReqGeneratorExecutionException, IOException {

		this.sourcePath = Files.createTempFile("", "");
		FileUtils.deleteQuietly(this.sourcePath.toFile());

		this.reqGeneratorConfigStub = this.getConfigStub();

		this.reqGenerator.configure(this.reqGeneratorConfigStub);

		thrown.expect(ReqGeneratorExecutionException.class);
		thrown.expectMessage("The path to lookup for source code coverage does not exist");
		this.reqGenerator.run();
	}

	@Test
	public void testRunWhenTestsPathIsUnexistent() throws ReqGeneratorConfigException, ReqGeneratorExecutionException, IOException {

		this.testsPath = Files.createTempFile("", "");
		FileUtils.deleteQuietly(this.testsPath.toFile());

		this.reqGeneratorConfigStub = this.getConfigStub();

		this.reqGenerator.configure(this.reqGeneratorConfigStub);

		thrown.expect(ReqGeneratorExecutionException.class);
		thrown.expectMessage("The path to lookup for tests code coverage does not exist");
		this.reqGenerator.run();
	}

	@Test
	public void testRunWhenItIsUnableToParseTheSourceOfDeclaredRequirements()
	        throws ReqGeneratorConfigException, ReqGeneratorExecutionException, IOException {

		this.declarationSource = Paths.get("i_bet_this_resource_does_not_exit").toUri();

		this.reqGeneratorConfigStub = this.getConfigStub();

		this.reqGenerator.configure(this.reqGeneratorConfigStub);

		thrown.expect(ReqGeneratorExecutionException.class);
		thrown.expectMessage("Error while parsing the source of declared requirements");
		thrown.expectCause(Is.isA(ReqSourceParserException.class));
		this.reqGenerator.run();
	}

	@Test
	public void testRunWhenItIsNotPossibleToWriteTheOutputCoverageReport()
	        throws ReqGeneratorConfigException, ReqGeneratorExecutionException, IOException {

		FileUtils.deleteQuietly(coverageOutputReportFile.toFile());
		Files.createDirectory(coverageOutputReportFile); // This will trigger the error since the abstract generator cannot generate a report into a
		                                                 // directory
		this.reqGeneratorConfigStub = this.getConfigStub();

		this.reqGenerator.configure(this.reqGeneratorConfigStub);

		thrown.expect(ReqGeneratorExecutionException.class);
		thrown.expectMessage("Error while marshalling the output coverage report : ");
		thrown.expectCause(Is.isA(JAXBException.class));
		this.reqGenerator.run();
	}

	private ReqGeneratorConfig getConfigStub() {
		final ReqGeneratorConfig config = new AbstractReqGeneratorConfig() {

			@Override
			public Path getSourceCodePath() {
				return sourcePath;
			}

			@Override
			public Path getTestsCodePath() {
				return testsPath;
			}

			@Override
			public URI getSourceRequirements() {
				return declarationSource;
			}

			@Override
			public ReqSourceParser getSourceParser() {
				return new FileReqSourceParser();
			}

			@Override
			public Path getCoverageOutput() {
				return coverageOutputReportFile;
			}
		};
		config.getFileIncludes().add("*");
		config.getFileExcludes().add("*.bin");
		return config;
	}

}
