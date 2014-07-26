<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;

class SlopeAdmin extends Admin {

    protected $baseRouteName = 'slope';
    protected $baseRoutePattern = 'slope';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('slopId')
                ->add('slopDescription')
                ->add('slopLength')
                ->add('slopDificulty');
    }

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('slopId')
                ->add('slopDescription')
                ->add('slopLength')
                ->add('slopDificulty');
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->add('slopId')
                ->add('slopDescription')
                ->add('slopLength')
                ->add('slopDificulty');
    }

}
