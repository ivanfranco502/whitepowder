<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Token
 */
class Token
{
    /**
     * @var integer
     */
    private $tokenId;

    /**
     * @var string
     */
    private $token;

    /**
     * @var \Tavros\DomainBundle\Entity\Users
     */
    private $tokenUser;


    /**
     * Get tokenId
     *
     * @return integer 
     */
    public function getTokenId()
    {
        return $this->tokenId;
    }

    /**
     * Set token
     *
     * @param string $token
     * @return Token
     */
    public function setToken($token)
    {
        $this->token = $token;

        return $this;
    }

    /**
     * Get token
     *
     * @return string 
     */
    public function getToken()
    {
        return $this->token;
    }

    /**
     * Set tokenUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $tokenUser
     * @return Token
     */
    public function setTokenUser(\Tavros\DomainBundle\Entity\Users $tokenUser = null)
    {
        $this->tokenUser = $tokenUser;

        return $this;
    }

    /**
     * Get tokenUser
     *
     * @return \Tavros\DomainBundle\Entity\Users 
     */
    public function getTokenUser()
    {
        return $this->tokenUser;
    }
}
