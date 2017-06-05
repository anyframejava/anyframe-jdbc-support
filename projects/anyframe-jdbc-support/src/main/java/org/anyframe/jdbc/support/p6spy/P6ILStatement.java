/*
 *
 * ====================================================================
 *
 * The P6Spy Software License, Version 1.1
 *
 * This license is derived and fully compatible with the Apache Software
 * license, see http://www.apache.org/LICENSE.txt
 *
 * Copyright (c) 2001-2002 Andy Martin, Ph.D. and Jeff Goke
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement:
 * "The original concept and code base for P6Spy was conceived
 * and developed by Andy Martin, Ph.D. who generously contribued
 * the first complete release to the public under this license.
 * This product was due to the pioneering work of Andy
 * that began in December of 1995 developing applications that could
 * seamlessly be deployed with minimal effort but with dramatic results.
 * This code is maintained and extended by Jeff Goke and with the ideas
 * and contributions of other P6Spy contributors.
 * (http://www.p6spy.com)"
 * Alternately, this acknowlegement may appear in the software itself,
 * if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "P6Spy", "Jeff Goke", and "Andy Martin" must not be used
 * to endorse or promote products derived from this software without
 * prior written permission. For written permission, please contact
 * license@p6spy.com.
 *
 * 5. Products derived from this software may not be called "P6Spy"
 * nor may "P6Spy" appear in their names without prior written
 * permission of Jeff Goke and Andy Martin.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package org.anyframe.jdbc.support.p6spy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.anyframe.jdbc.support.CompleteQueryPostProcessor;
import org.anyframe.jdbc.support.InjectionPatternPostProcessor;

import com.p6spy.engine.spy.P6Connection;
import com.p6spy.engine.spy.P6CoreFactory;
import com.p6spy.engine.spy.P6Statement;

/**
 * P6Spy Statement Extension for jdbc support functions (InjectionPattern,
 * CompleteQuery)
 *
 * @author Changje Kim
 * @author Byunghun Woo
 *
 */
public class P6ILStatement extends P6Statement {

	private InjectionPatternPostProcessor injectionPatternPostProcessor;

	private CompleteQueryPostProcessor completeQueryPostProcessor;

	public P6ILStatement(P6CoreFactory factory, Statement statement, P6Connection conn,
			InjectionPatternPostProcessor injectionPatternPostProcessor,
			CompleteQueryPostProcessor completeQueryPostProcessor) {
		super(factory, statement, conn);
		this.injectionPatternPostProcessor = injectionPatternPostProcessor;
		this.completeQueryPostProcessor = completeQueryPostProcessor;
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		injectionPatternPostProcessor.warningPattern(sql);
		statementQuery = injectionPatternPostProcessor.replacePattern(sql);
		completeQueryPostProcessor.processCompleteQuery(statementQuery);

		return passthru.execute(statementQuery);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		injectionPatternPostProcessor.warningPattern(sql);
		statementQuery = injectionPatternPostProcessor.replacePattern(sql);
		completeQueryPostProcessor.processCompleteQuery(statementQuery);

		return getP6Factory().getResultSet(passthru.executeQuery(statementQuery), this, "", statementQuery);
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		injectionPatternPostProcessor.warningPattern(sql);
		statementQuery = injectionPatternPostProcessor.replacePattern(sql);
		completeQueryPostProcessor.processCompleteQuery(statementQuery);

		return (passthru.executeUpdate(sql));
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		injectionPatternPostProcessor.warningPattern(sql);
		statementQuery = injectionPatternPostProcessor.replacePattern(sql);
		completeQueryPostProcessor.processCompleteQuery(statementQuery);

		passthru.addBatch(statementQuery);
	}

	@Override
	public int[] executeBatch() throws SQLException {
		injectionPatternPostProcessor.warningPattern(statementQuery);
		statementQuery = injectionPatternPostProcessor.replacePattern(statementQuery);
		completeQueryPostProcessor.processCompleteQuery(statementQuery);

		return (passthru.executeBatch());
	}
}
