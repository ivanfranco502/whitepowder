<?php

namespace Tavros\DomainBundle\Repositories;

use Doctrine\ORM\EntityRepository;

class UsersRepository extends EntityRepository {

    public function findAllRescuer() {
        $em = $this->getEntityManager();
        $users = $em->getRepository('TavrosDomainBundle:Users')->findAll();
        $rescuers = array();

        foreach ($users as $user) {

            $roles = $user->getRoles();
            foreach ($roles as $r) {
                if ($r === 'ROLE_SKIER') {
                    $role = 'ROLE_SKIER';
                    break;
                } elseif ($r === 'ROLE_RECON') {
                    $role = 'ROLE_RECON';
                    break;
                } else {
                    $role = 'ROLE_RESCU';
                    break;
                }
            }

            if ($role == 'ROLE_RESCU') {
                $rescuers[] = $user;
            }
        }
        
        return $rescuers;
    }

}
