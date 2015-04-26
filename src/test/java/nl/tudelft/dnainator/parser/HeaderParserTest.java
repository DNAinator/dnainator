package nl.tudelft.dnainator.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import nl.tudelft.dnainator.core.Sequence;
import nl.tudelft.dnainator.core.SequenceFactory;

import org.junit.Test;

public class HeaderParserTest {

	@Test
	public void testParseHeaderGood() {
		HeaderParser hp = new HeaderParser("1 | a,b,c | 3 | 5");
		try {
			assertEquals("1", hp.next());
			assertEquals("a,b,c", hp.next());
			assertEquals("3", hp.next());
			assertEquals("5", hp.next());
		} catch (InvalidHeaderFormatException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testParseHeaderTooLittle() {
		HeaderParser hp = new HeaderParser("1 | a,b,c | 3");
		try {
			assertEquals("1", hp.next());
			assertEquals("a,b,c", hp.next());
			assertEquals("3", hp.next());
			assertEquals("5", hp.next());
		} catch (InvalidHeaderFormatException e) {
			assertEquals("Not enough tokens, wanted 4, got 3", e.getMessage());
		}
	}

	@Test
	public void testParseHeaderTooMuch() {
		HeaderParser hp = new HeaderParser("1 | a,b,c | 3 | 5 | 6");
		try {
			assertEquals("1", hp.next());
			assertEquals("a,b,c", hp.next());
			assertEquals("3", hp.next());
			assertEquals("5", hp.next());
			assertEquals("6", hp.next());
		} catch (InvalidHeaderFormatException e) {
			assertEquals("Leftover string:  6", e.getMessage());
		}
	}

	@Test(expected = NumberFormatException.class)
	public void testParseHeaderNumberException() throws Exception {
		HeaderParser hp = new HeaderParser("1 | a,b,c | no number!! | 6");
		hp.fill(new SequenceFactory() {
			@Override
			public void setContent(String content) {
			}

			@Override
			public Sequence build(String refs, int startPos, int endPos) {
				return null;
			}
		});
	}

}
