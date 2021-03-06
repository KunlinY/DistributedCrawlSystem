/*
 * This file is generated by jOOQ.
*/
package com.jooq.data.tables;


import com.jooq.data.Keys;
import com.jooq.data.Public;
import com.jooq.data.tables.records.PersonsRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Persons extends TableImpl<PersonsRecord> {

    private static final long serialVersionUID = -1032613701;

    /**
     * The reference instance of <code>public.persons</code>
     */
    public static final Persons PERSONS = new Persons();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PersonsRecord> getRecordType() {
        return PersonsRecord.class;
    }

    /**
     * The column <code>public.persons.id</code>.
     */
    public final TableField<PersonsRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('persons_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.persons.person</code>.
     */
    public final TableField<PersonsRecord, String> PERSON = createField("person", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.persons.ulist</code>.
     */
    public final TableField<PersonsRecord, Long[]> ULIST = createField("ulist", org.jooq.impl.SQLDataType.BIGINT.getArrayDataType(), this, "");

    /**
     * Create a <code>public.persons</code> table reference
     */
    public Persons() {
        this("persons", null);
    }

    /**
     * Create an aliased <code>public.persons</code> table reference
     */
    public Persons(String alias) {
        this(alias, PERSONS);
    }

    private Persons(String alias, Table<PersonsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Persons(String alias, Table<PersonsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<PersonsRecord, Long> getIdentity() {
        return Keys.IDENTITY_PERSONS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PersonsRecord> getPrimaryKey() {
        return Keys.PERSONS_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PersonsRecord>> getKeys() {
        return Arrays.<UniqueKey<PersonsRecord>>asList(Keys.PERSONS_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Persons as(String alias) {
        return new Persons(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Persons rename(String name) {
        return new Persons(name, null);
    }
}
