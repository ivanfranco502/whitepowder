<?php

namespace Tavros\InternalApiBundle\Entity;

/**
 * ApiResponse
 */
class ApiResponse {

    /**
     * @var string
     */
    private $code;

    /**
     * @var string
     */
    private $payload = '';

    /**
     * Get code
     *
     * @return integer 
     */
    public function getCode() {
        return $this->code;
    }

    /**
     * Set code
     *
     * @return ApiResponse
     */
    public function setCode($code) {
        $this->code = $code;

        return $this;
    }

    /**
     * Get payload
     *
     * @return integer 
     */
    public function getPayload() {
        return $this->payload;
    }

    /**
     * Set payload
     *
     * @return ApiResponse
     */
    public function setPayload($payload) {
        $this->payload = $payload;

        return $this;
    }

}
