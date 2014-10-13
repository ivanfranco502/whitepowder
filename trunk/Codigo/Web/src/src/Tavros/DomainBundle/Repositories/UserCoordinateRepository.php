<?php

namespace Tavros\DomainBundle\Repositories;

use Doctrine\ORM\EntityRepository;

class UserCoordinateRepository extends EntityRepository {

    public function findAllLastPosition() {
        $sql = "SELECT *
                FROM user_coordinate
                WHERE usco_id IN (
                                    SELECT MAX(usco_id)
                                    FROM user_coordinate
                                    GROUP BY usco_user_id)
                    AND usco_ski_mode = 1";

        $stmt = $this->getEntityManager()->getConnection()->prepare($sql);
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public function findLastPosition($id) {
        $sql = "SELECT *
                FROM user_coordinate
                WHERE usco_id IN (
                                    SELECT MAX(usco_id)
                                    FROM user_coordinate
                                    GROUP BY usco_user_id
                                    HAVING usco_user_id = $id)";

        $stmt = $this->getEntityManager()->getConnection()->prepare($sql);
        $stmt->execute();
        return $stmt->fetchAll();
    }

}
