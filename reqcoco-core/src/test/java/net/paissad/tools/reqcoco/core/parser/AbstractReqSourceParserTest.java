package net.paissad.tools.reqcoco.core.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.xml.bind.UnmarshalException;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.paissad.tools.reqcoco.api.exception.ReqSourceParserException;
import net.paissad.tools.reqcoco.api.model.Requirement;
import net.paissad.tools.reqcoco.api.model.Requirements;
import net.paissad.tools.reqcoco.api.model.Version;
import net.paissad.tools.reqcoco.api.parser.ReqSourceParser;
import net.paissad.tools.reqcoco.core.TestUtil;

public class AbstractReqSourceParserTest {

	private ReqSourceParser		requirementSourceParser;

	@Rule
	public ExpectedException	thrown	= ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		this.setUpByUsingUri(TestUtil.REQUIREMENTS_INPUT_FILE1_XML_URI);
	}

	@Test
	public void testGetRequirements() throws ReqSourceParserException {
		final Requirements reqs = requirementSourceParser.getRequirements();
		Assert.assertNotNull(reqs);
		Assert.assertEquals(3, reqs.getRequirements().stream().count());
	}

	/**
	 * The XML source does not have a well formatted content.
	 * 
	 * @throws ReqSourceParserException
	 */
	@Test
	public void testGetRequirementsBadContent() throws ReqSourceParserException {

		thrown.expect(ReqSourceParserException.class);
		thrown.expectCause(Is.isA(UnmarshalException.class));
		thrown.expectMessage("Error while retrieving requirements from the source : ");

		this.setUpByUsingUri(TestUtil.REQUIREMENTS_INTPUT_MALFORMED_SOURCE1_XML_URI);
		this.requirementSourceParser.getRequirements();
	}

	@Test
	public void testGetRequirementsNotSupportedUriScheme() throws ReqSourceParserException, URISyntaxException {
		thrown.expectCause(Is.isA(UnsupportedOperationException.class));
		thrown.expectMessage("Unable to parse source from the scheme type --> foobar");
		requirementSourceParser = TestUtil.initAbstractRequirementSourceParser(new URI("foobar://not_supported_scheme/resource.xml"), null);
		requirementSourceParser.getRequirements();
	}

	@Test
	public void testGetRequirementsBadFormattedScheme() throws ReqSourceParserException, URISyntaxException {
		thrown.expect(URISyntaxException.class);
		TestUtil.initAbstractRequirementSourceParser(new URI("123badscheme://very_bad_scheme/resource.xml"), null);
	}

	@Test
	public void testGetRequirementsNullUri() throws ReqSourceParserException, URISyntaxException {
		thrown.expectCause(Is.isA(NullPointerException.class));
		thrown.expectMessage("The URI to parse should is null");
		requirementSourceParser = TestUtil.initAbstractRequirementSourceParser(null, null);
		requirementSourceParser.getRequirements();
	}

	@Test
	public void testGetRequirementsVersion() throws ReqSourceParserException {
		final Version v1 = new Version();
		v1.setValue("1.0");
		Collection<Requirement> reqs = requirementSourceParser.getRequirements(v1);
		Assert.assertEquals(2, reqs.size());

		final Version v1_1 = new Version();
		v1_1.setValue("1.1");
		reqs = requirementSourceParser.getRequirements(v1_1);
		Assert.assertEquals(1, reqs.size());
	}

	@Test
	public void testGetURI() throws URISyntaxException {
		final AbstractReqSourceParser parser = (AbstractReqSourceParser) requirementSourceParser;
		Assert.assertTrue(parser.getURI().toString().matches(Pattern.quote(TestUtil.REQUIREMENTS_INPUT_FILE1_XML_URI.toString())));
	}

	private void setUpByUsingUri(final URI uri) throws ReqSourceParserException {
		this.requirementSourceParser = TestUtil.initAbstractRequirementSourceParser(uri, null);
	}
}
