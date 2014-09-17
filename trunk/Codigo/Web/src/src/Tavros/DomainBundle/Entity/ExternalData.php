<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * ExternalData
 */
class ExternalData
{
    /**
     * @var integer
     */
    private $exdaId;

    /**
     * @var string
     */
    private $exdaToken;

    /**
     * @var string
     */
    private $exdaRegistrationCode;

    /**
     * @var \Tavros\DomainBundle\Entity\Users
     */
    private $exdaUser;


    /**
     * Get exdaId
     *
     * @return integer 
     */
    public function getExdaId()
    {
        return $this->exdaId;
    }

    /**
     * Set exdaToken
     *
     * @param string $exdaToken
     * @return ExternalData
     */
    public function setExdaToken($exdaToken)
    {
        $this->exdaToken = $exdaToken;

        return $this;
    }

    /**
     * Get exdaToken
     *
     * @return string 
     */
    public function getExdaToken()
    {
        return $this->exdaToken;
    }

    /**
     * Set exdaRegistrationCode
     *
     * @param string $exdaRegistrationCode
     * @return ExternalData
     */
    public function setExdaRegistrationCode($exdaRegistrationCode)
    {
        $this->exdaRegistrationCode = $exdaRegistrationCode;

        return $this;
    }

    /**
     * Get exdaRegistrationCode
     *
     * @return string 
     */
    public function getExdaRegistrationCode()
    {
        return $this->exdaRegistrationCode;
    }

    /**
     * Set exdaUser
     *
     * @param \Tavros\DomainBundle\Entity\Users $exdaUser
     * @return ExternalData
     */
    public function setExdaUser(\Tavros\DomainBundle\Entity\Users $exdaUser = null)
    {
        $this->exdaUser = $exdaUser;

        return $this;
    }

    /**
     * Get exdaUser
     *
     * @return \Tavros\DomainBundle\Entity\Users 
     */
    public function getExdaUser()
    {
        return $this->exdaUser;
    }
}
