<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;

class NotificationTypeAdmin extends Admin {

    protected $baseRouteName = 'notificationtype';
    protected $baseRoutePattern = 'notificationtype';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('notyDescription', 'text', array('label' => 'Notificación'));
    }

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('notyDescription', null, array('label' => 'Notificación'));
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->addIdentifier('notyDescription', null, array('label' => 'Notificación'));
    }

}
