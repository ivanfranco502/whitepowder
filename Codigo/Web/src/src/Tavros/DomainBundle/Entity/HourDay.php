<?php

namespace Tavros\DomainBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * HourDay
 */
class HourDay
{
    /**
     * @var integer
     */
    private $hodaId;

    /**
     * @var string
     */
    private $hodaDay;

    /**
     * @var string
     */
    private $hodaStartHour;

    /**
     * @var string
     */
    private $hodaEndHour;

    /**
     * @var boolean
     */
    private $hodaClose;

    /**
     * @var \Tavros\DomainBundle\Entity\GeneralInformation
     */
    private $hodaGeneralInformation;


    public function __toString() {
        return $this->hodaDay . " - " . $this->hodaStartHour . " - " . $this->hodaEndHour;
    }
    /**
     * Get hodaId
     *
     * @return integer 
     */
    public function getHodaId()
    {
        return $this->hodaId;
    }

    /**
     * Set hodaDay
     *
     * @param string $hodaDay
     * @return HourDay
     */
    public function setHodaDay($hodaDay)
    {
        $this->hodaDay = $hodaDay;

        return $this;
    }

    /**
     * Get hodaDay
     *
     * @return string 
     */
    public function getHodaDay()
    {
        return $this->hodaDay;
    }

    /**
     * Set hodaStartHour
     *
     * @param string $hodaStartHour
     * @return HourDay
     */
    public function setHodaStartHour($hodaStartHour)
    {
        $this->hodaStartHour = $hodaStartHour;

        return $this;
    }

    /**
     * Get hodaStartHour
     *
     * @return string 
     */
    public function getHodaStartHour()
    {
        return $this->hodaStartHour;
    }

    /**
     * Set hodaEndHour
     *
     * @param string $hodaEndHour
     * @return HourDay
     */
    public function setHodaEndHour($hodaEndHour)
    {
        $this->hodaEndHour = $hodaEndHour;

        return $this;
    }

    /**
     * Get hodaEndHour
     *
     * @return string 
     */
    public function getHodaEndHour()
    {
        return $this->hodaEndHour;
    }

    /**
     * Set hodaClose
     *
     * @param boolean $hodaClose
     * @return HourDay
     */
    public function setHodaClose($hodaClose)
    {
        $this->hodaClose = $hodaClose;

        return $this;
    }

    /**
     * Get hodaClose
     *
     * @return boolean 
     */
    public function getHodaClose()
    {
        return $this->hodaClose;
    }

    /**
     * Set hodaGeneralInformation
     *
     * @param \Tavros\DomainBundle\Entity\GeneralInformation $hodaGeneralInformation
     * @return HourDay
     */
    public function setHodaGeneralInformation(\Tavros\DomainBundle\Entity\GeneralInformation $hodaGeneralInformation = null)
    {
        $this->hodaGeneralInformation = $hodaGeneralInformation;

        return $this;
    }

    /**
     * Get hodaGeneralInformation
     *
     * @return \Tavros\DomainBundle\Entity\GeneralInformation 
     */
    public function getHodaGeneralInformation()
    {
        return $this->hodaGeneralInformation;
    }
}
