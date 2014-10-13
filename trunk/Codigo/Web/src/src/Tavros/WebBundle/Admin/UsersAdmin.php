<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;
use Sonata\AdminBundle\Route\RouteCollection;

class UsersAdmin extends Admin {

    protected $baseRouteName = 'users';
    protected $baseRoutePattern = 'users';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('username', null,
                        array('label' => 'Username',
                                'read_only' => true,
                                'disabled'  => true))
                ->add('email', null, 
                        array('label' => 'E-mail',
                                'read_only' => true,
                                'disabled'  => true))
                ->add('enabled', null, array('label' => 'Habilitado'))
                ->add('roles', null, array('label' => 'Roles'));
    }

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('username', null, array('label' => 'Username'))
                ->add('email', null, array('label' => 'E-mail'))
                ->add('enabled', null, array('label' => 'Habilitado'))
                ->add('lastLogin', null, array('label' => 'Último login'))
                ->add('roles', null, array('label' => 'Roles'));
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->addIdentifier('username', null, array('label' => 'Username'))
                ->add('email', null, array('label' => 'E-mail'))
                ->add('enabled', null, array('label' => 'Habilitado'))
                ->add('lastLogin', null, array('label' => 'Último login'))
                ->add('roles', null, array('label' => 'Roles'));
    }

    protected function configureRoutes(RouteCollection $collection) {
        // to remove a single route
        // $collection->remove('delete');
        // OR remove all route except named ones
        $collection->clearExcept(array('edit', 'show', 'list'));
    }

}
