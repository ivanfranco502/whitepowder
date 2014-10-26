<?php

namespace Tavros\DomainBundle\Repositories;

use Doctrine\ORM\EntityRepository;

class AlertRepository extends EntityRepository {

    public function markAllAsRead() {
        $sql = "UPDATE alert SET aler_read = 1 WHERE 1";
        $stmt = $this->getEntityManager()->getConnection()->prepare($sql);
        $stmt->execute();
        return true;
    }

}
