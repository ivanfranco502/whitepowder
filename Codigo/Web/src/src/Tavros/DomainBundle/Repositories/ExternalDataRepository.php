<?php

namespace Tavros\DomainBundle\Repositories;

use Doctrine\ORM\EntityRepository;

class ExternalDataRepository extends EntityRepository {

    public function findAllRegistrationId() {
        $sql = "select exda_registration_code from (select * from "
                . "(select * from user_coordinate ORDER BY usco_update_date DESC) "
                . "AS X GROUP BY usco_user_id) as d left join external_data on d.usco_user_id = exda_user_id "
                . "where usco_ski_mode = 1";

        $stmt = $this->getEntityManager()->getConnection()->prepare($sql);
        $stmt->execute();
        return $stmt->fetchAll();
    }

}
