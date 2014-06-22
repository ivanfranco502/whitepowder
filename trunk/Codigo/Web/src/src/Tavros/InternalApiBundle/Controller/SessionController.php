<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\JsonResponse;

class SessionController extends Controller {

    public function loginAction(Request $request) {

        $username = $request->get('username');
        $password = $request->get('password');

        if ($username && $password) {
            $response = new JsonResponse(array(
                'status' => "SUCCESS",
                'code' => 2,
                'message' => "Welcome " . $username));
        } else {
            $response = new JsonResponse(array(
                'status' => "ERROR",
                'code' => 1,
                'message' => "Neither the username or the password has been specifed."));
        }
        return $response;
    }

}
