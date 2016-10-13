package com.ervacon.swfextensions.conversation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.impl.SimpleConversationId;

/**
 * JDBC based implementation of the {@link ConversationDao} interface. Needs to be
 * configured with a JDBC DataSource, just like any other Spring JdbcDaoSupport
 * based DAO.
 * <p>
 * Assumes a table structure like this (HSQLDB syntax):
 * <pre>
 *		create table CONVERSATION(
 *			ID char(48) primary key,
 *			ATTRIBUTES VARBINARY not null
 *		);
 * </pre>
 * <p>
 * This example is discussed in the book "Working with Spring Web Flow" by Erwin Vervaet,
 * ISBN 978-90-812141-1-7.
 * 
 * @author Erwin Vervaet
 */
public class JdbcConversationDao extends SimpleJdbcDaoSupport implements ConversationDao {

	public void createConversation(PersistentConversation conv) {
		getSimpleJdbcTemplate().update(
				"insert into CONVERSATION(ID, ATTRIBUTES) values (?, ?)",
				conv.getId().toString(), conv.getAttributesAsByteArray());
	}

	public PersistentConversation readConversation(ConversationId id) {
		List<PersistentConversation> res = getSimpleJdbcTemplate().query(
			"select ID, ATTRIBUTES from CONVERSATION where ID = ?",
			new ParameterizedRowMapper<PersistentConversation>() {
				public PersistentConversation mapRow(ResultSet rs, int rowNum) throws SQLException {
					try {
						PersistentConversation conv = new PersistentConversation();
						conv.setId(new SimpleConversationId(rs.getString("ID")));
						conv.setAttributesAsByteArray(rs.getBytes("ATTRIBUTES"));
						conv.setDao(JdbcConversationDao.this);
						return conv;
					}
					catch (Exception e) {
						// should not happen
						throw new SQLException(e);
					}
				}
			},
			id.toString());
		return res.isEmpty() ? null : res.get(0);
	}

	public void updateConversation(PersistentConversation conv) {
		getSimpleJdbcTemplate().update(
				"update CONVERSATION set ATTRIBUTES = ? where ID = ?",
				conv.getAttributesAsByteArray(), conv.getId().toString());
	}

	public void deleteConversation(ConversationId id) {
		getSimpleJdbcTemplate().update(
				"delete from CONVERSATION where ID = ?",
				id.toString());
	}
}
