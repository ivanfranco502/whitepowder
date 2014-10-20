<?php

namespace Tavros\DomainBundle\Entity;

use FOS\UserBundle\Entity\User as BaseUser;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity
 * @ORM\Table(name="fos_user")
 */

class Users extends BaseUser {

    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $id;

    public function __construct() {
        parent::__construct();
        // your own logic
    }
    
    /**
    * @return string
    */
    public function getRolesAsString()
    {
        $roles = array();
        foreach ($this->getRoles() as $role) {
           $role = explode('_', $role);
           array_shift($role);
           $roles[] = ucfirst(strtolower(implode(' ', $role)));
        }

        return implode(', ', $roles);
    }

}