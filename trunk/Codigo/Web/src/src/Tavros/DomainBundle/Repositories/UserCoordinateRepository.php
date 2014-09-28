<?php

namespace Tavros\DomainBundle\Repositories;

use Doctrine\ORM\EntityRepository;

class UserCoordinateRepository extends EntityRepository {

    public function findAllLastPosition() {
        $sql = "select * from (select * from (select * from user_coordinate ORDER BY usco_update_date DESC)"
                . "AS X GROUP BY usco_user_id) as d where usco_ski_mode = 1";

        $stmt = $this->getEntityManager()->getConnection()->prepare($sql);
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public function findLastPosition($user) {
        $sql = "SELECT * FROM 
                    (SELECT * FROM 
                        (SELECT * FROM user_coordinate ORDER BY usco_update_date DESC ) AS X GROUP BY usco_user_id) AS Z
                WHERE usco_user_id = " . $user->getid();

        $stmt = $this->getEntityManager()->getConnection()->prepare($sql);
        $stmt->execute();
        return $stmt->fetchAll();
    }

}
