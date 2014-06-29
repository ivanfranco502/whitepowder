<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\JsonResponse;

class UserController extends Controller {

    public function pruebaAction(Request $request) {

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

    public function registerAction(Request $resquest) {

        $userManager = $this->get('fos_user.user_manager');

        $newUser = $userManager->createUser();

        $response = new JsonResponse(array($newUser->getId()));
        return $response;
    }

}
