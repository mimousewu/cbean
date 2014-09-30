package net.cbean.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.tools.ant.taskdefs.SQLExec;

public class ProcedureDDLSQLExec extends SQLExec {

	/**
	 * key: keyword value: have name in end statement or not
	 */
	private static Map<String, Boolean> keywords = new HashMap<String, Boolean>();
	static {
		keywords.put("FUNCTION", true);
		keywords.put("PROCEDURE", true);
		keywords.put("JOB", false);
	}

	private int blockStack = -1;
	private boolean endBlock = false;

	private boolean keepformat;

	private String otherDelimitor;

	@Override
	protected void runStatements(Reader reader, PrintStream out)
			throws SQLException, IOException {
		StringBuffer sql = new StringBuffer();
		String line;

		BufferedReader in = new BufferedReader(reader);

		while ((line = in.readLine()) != null) {
			if (!keepformat) {
				line = line.trim();
			}
			if (super.getExpandProperties()) {
				line = getProject().replaceProperties(line);
			}
			if (!keepformat) {
				if (line.startsWith("//")) {
					continue;
				}
				if (line.startsWith("--")) {
					continue;
				}
				StringTokenizer st = new StringTokenizer(line);
				if (st.hasMoreTokens()) {
					String token = st.nextToken();
					if ("REM".equalsIgnoreCase(token)) {
						continue;
					}

					if (blockStack >= 0) {
						if (token.toUpperCase().startsWith("BEGIN")) {
							blockStack++;
						}
					}
					if (token.toUpperCase().startsWith("END")) {
						if (otherDelimitor == null) {
							blockStack--;
							endBlock = true; // Just for job, if contains end in
												// job block, it should be wrong
						} else {
							if (otherDelimitor.equals(st.nextToken())) {
								blockStack--;
								endBlock = true;
							}
						}
					} else {
						boolean isBlock = keywords.containsKey(token
								.toUpperCase());
						Boolean hasNameInEnds = keywords.get(token
								.toUpperCase());
						while (!isBlock && st.hasMoreTokens()) {
							String nextToken = st.nextToken().toUpperCase();
							if (keywords.containsKey(nextToken)) {
								isBlock = true;
								hasNameInEnds = keywords.get(nextToken);
								break;
							}
						}

						if (isBlock) {
							blockStack = 0;
							if (hasNameInEnds != null && hasNameInEnds) {
								otherDelimitor = st.nextToken().replaceFirst(
										"[^A-Za-z_]+.*$", "");
							}
							endBlock = false;
						}
					}
				}
			}

			sql.append("\n").append(line);

			// SQL defines "--" as a comment to EOL
			// and in Oracle it may contain a hint
			// so we cannot just remove it, instead we must end it
			if (!keepformat && line.indexOf("--") >= 0) {
				sql.append("\n");
			}
			int lastDelimPos = lastDelimiterPosition(sql, line);
			if (blockStack >= 0 && !endBlock) {
				lastDelimPos = -1;
			}
			if (lastDelimPos > -1) {
				int withColon = (blockStack >= 0) ? 1 : 0;
				execSQL(sql.substring(0, lastDelimPos + withColon), out);
				clearBlock();
				sql.replace(0, sql.length(), "");
			}
		}
		// Catch any statements not followed by ;
		if (sql.length() > 0) {
			execSQL(sql.toString(), out);
			clearBlock();
		}
	}

	private void clearBlock() {
		blockStack = -1;
		otherDelimitor = null;
		endBlock = false;
	}

	public void setKeepformat(boolean keepformat) {
		this.keepformat = keepformat;
		super.setKeepformat(keepformat);
	}

}
