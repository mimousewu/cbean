package net.cbean.binary;

import junit.framework.TestCase;

public class BinMappingTest extends TestCase {

	/*
	 * Test method for 'net.cbean.binary.BinMapping.loadMapping(String)'
	 */
	public void testLoadMappingString() throws Exception {
		BinMapping mapping = BinMapping.loadMapping("NodeB_mit.xml");
		ParserHandle parseHandler = new ParserHandle(){

			public BinParser getParser(String parserName) {
				// Auto-generated method stub
				return null;
			}

			public <T> BinParser<T> getParser(String parserName, Class<T> t) {
				// Auto-generated method stub
				return null;
			}

			public void setParser(String key, BinParser parser) {
				// Auto-generated method stub
				
			}};
		BinMarshaller b = new BinMarshaller(mapping,parseHandler);
		assertNotNull(mapping);
	}

}
