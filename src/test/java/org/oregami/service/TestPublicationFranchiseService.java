package org.oregami.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oregami.data.PublicationFranchiseDao;
import org.oregami.entities.Publication;
import org.oregami.entities.PublicationFranchise;
import org.oregami.entities.PublicationIssue;
import org.oregami.test.PersistenceTest;
import org.oregami.util.StartHelper;

import javax.persistence.EntityManager;
import java.util.List;

public class TestPublicationFranchiseService {


    private static Injector injector;

    private static EntityManager entityManager;

    public TestPublicationFranchiseService() {
    }

    @BeforeClass
    public static void init() {
        StartHelper.init(StartHelper.CONFIG_FILENAME_TEST);
        injector = StartHelper.getInjector();
        entityManager = injector.getInstance(EntityManager.class);
    }

    @Test
    public void testBasicPersistence() {

        entityManager.getTransaction().begin();

        PublicationFranchise pf = new PublicationFranchise();
        pf.setName("Power Play");

        entityManager.persist(pf);
        entityManager.flush();

        PublicationFranchiseDao dao = injector.getInstance(PublicationFranchiseDao.class);
        List<PublicationFranchise> list = dao.findAll();

        Assert.assertEquals(1, list.size());

        entityManager.getTransaction().rollback();
    }


    @Test
    public void testServiceUpdate() {

        entityManager.getTransaction().begin();

        PublicationFranchise pf = new PublicationFranchise();
        pf.setName("Power Play");

        entityManager.persist(pf);
        entityManager.flush();

        PublicationFranchiseDao dao = injector.getInstance(PublicationFranchiseDao.class);
        List<PublicationFranchise> list = dao.findAll();

        Assert.assertEquals(1, list.size());


        PublicationFranchiseService service = injector.getInstance(PublicationFranchiseService.class);
        PublicationFranchise publicationFranchise = dao.findOne(pf.getId());
        publicationFranchise.setName("updated");
        ServiceResult<PublicationFranchise> serviceResult = service.updateEntity(publicationFranchise, null);
        Assert.assertTrue(serviceResult.getErrors().isEmpty());

        PublicationFranchise franchiseLoaded = dao.findOne(pf.getId());
        Assert.assertEquals(publicationFranchise.getName(), franchiseLoaded.getName());

        franchiseLoaded.setName("a");
        Publication publication = new Publication();
        publication.setName("pub name");
        PublicationIssue issue1 = new PublicationIssue();
        publication.getPublicationIssueList().add(issue1);
        franchiseLoaded.getPublicationList().add(publication);
        ServiceResult<PublicationFranchise> serviceResult2 = service.updateEntity(franchiseLoaded, null);

        Assert.assertTrue(serviceResult2.hasErrors());
        Assert.assertEquals(3, serviceResult2.getErrors().size());

        entityManager.getTransaction().rollback();
    }

}
