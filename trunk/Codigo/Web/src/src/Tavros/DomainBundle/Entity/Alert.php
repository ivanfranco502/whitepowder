<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Alert
 */
class Alert
{
    /**
     * @var integer
     */
    private $alerId;

    /**
     * @var float
     */
    private $alerXPosition;

    /**
     * @var float
     */
    private $alerYPosition;

    /**
     * Get alerId
     *
     * @return integer 
     */
    public function getAlerId()
    {
        return $this->alerId;
    }

    /**
     * Set alerXPosition
     *
     * @param float $alerXPosition
     * @return Alert
     */
    public function setAlerXPosition($alerXPosition)
    {
        $this->alerXPosition = $alerXPosition;

        return $this;
    }

    /**
     * Get alerXPosition
     *
     * @return float 
     */
    public function getAlerXPosition()
    {
        return $this->alerXPosition;
    }

    /**
     * Set alerYPosition
     *
     * @param float $alerYPosition
     * @return Alert
     */
    public function setAlerYPosition($alerYPosition)
    {
        $this->alerYPosition = $alerYPosition;

        return $this;
    }

    /**
     * Get alerYPosition
     *
     * @return float 
     */
    public function getAlerYPosition()
    {
        return $this->alerYPosition;
    }
    
}
