package org.oregami.test;

import com.google.inject.Injector;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.data.*;
import org.oregami.entities.*;
import org.oregami.entities.datalist.Script;
import org.oregami.entities.datalist.TitleType;
import org.oregami.util.StartHelper;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by sebastian on 25.04.15.
 */
public class TransliteratedStringTest {

    private static Injector injector;

    static EntityManager entityManager = null;

    @BeforeClass
    public static void initClass() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
        injector = StartHelper.getInjector();
        entityManager = injector.getInstance(EntityManager.class);
        injector.getInstance(DatabaseFiller.class).addLanguages();
        injector.getInstance(DatabaseFiller.class).addRegions();
        injector.getInstance(BaseListFiller.class).initBaseLists();
    }

    @AfterClass
    public static void afterClass() {
        injector.getInstance(DatabaseFiller.class).dropAllData();
    }

    @Test
    public void saveAndReadEntity() {
        entityManager.getTransaction().begin();

        TransliteratedStringDao dao = injector.getInstance(TransliteratedStringDao.class);

        TransliteratedString s = new TransliteratedString();
        Language l = injector.getInstance(LanguageDao.class).findByExactName(Language.ENGLISH);
        s.setLanguage(l);
        Script script = injector.getInstance(BaseListFinder.class).getScript(Script.LATIN);
        s.setScript(script);
        s.setText("a latin text");

        dao.save(s);

        String id = s.getId();

        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        TransliteratedString loadedEntity = dao.findOne(id);
        System.out.println(loadedEntity);
        Assert.assertThat(loadedEntity, Matchers.is(Matchers.notNullValue()));
        Assert.assertThat(loadedEntity.getId(), Matchers.is(id));
        Assert.assertThat(loadedEntity.getText(), Matchers.is(s.getText()));
        entityManager.getTransaction().commit();

    }

    @Test
    public void saveAndReadEntityChinese() {
        entityManager.getTransaction().begin();

        TransliteratedStringDao dao = injector.getInstance(TransliteratedStringDao.class);

        TransliteratedString s = new TransliteratedString();
        Language l = injector.getInstance(LanguageDao.class).findByExactName(Language.CHINESE);
        s.setLanguage(l);
        Script script = injector.getInstance(BaseListFinder.class).getScript(Script.CHINESE);
        s.setScript(script);
        s.setText("计算机"); //chinese for "computer"

        dao.save(s);

        String id = s.getId();

        entityManager.getTransaction().commit();


        entityManager.getTransaction().begin();
        TransliteratedString loadedEntity = dao.findOne(id);
        System.out.println(loadedEntity);
        Assert.assertThat(loadedEntity, Matchers.is(Matchers.notNullValue()));
        Assert.assertThat(loadedEntity.getId(), Matchers.is(id));
        Assert.assertThat(loadedEntity.getText(), Matchers.is(s.getText()));
        entityManager.getTransaction().commit();

    }


}
