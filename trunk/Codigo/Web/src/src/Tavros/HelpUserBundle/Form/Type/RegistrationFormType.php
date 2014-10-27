<?php

namespace Tavros\HelpUserBundle\Form\Type;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;

class RegistrationFormType extends AbstractType {

    public function buildForm(FormBuilderInterface $builder, array $options) {
        $builder
                ->add('username', null, array('label' => 'form.username', 'translation_domain' => 'FOSUserBundle', 'attr' => array('class' => 'input-group col-sm-12')))
                ->add('email', 'email', array('label' => 'form.email', 'translation_domain' => 'FOSUserBundle', 'attr' => array('class' => 'input-group col-sm-12')))
                ->add('plainPassword', 'repeated', array(
                    'type' => 'password',
                    'options' => array('translation_domain' => 'FOSUserBundle'),
                    'first_options' => array('label' => 'form.password', 'attr' => array('class' => 'input-group col-sm-12')),
                    'second_options' => array('label' => 'form.password_confirmation', 'attr' => array('class' => 'input-group col-sm-12')),
                    'invalid_message' => 'fos_user.password.mismatch',
        ));
    }

    public function getName() {
        return 'tavros_help_user_registration';
    }

    public function getParent() {
        return 'fos_user_registration';
    }

}
