/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	config.toolbar = 'Full';
    config.toolbar_Full = [
        {name: 'document',items: ['Source', '-', 'DocProps', 'Preview', 'Print', '-']},
        {name: 'clipboard',items: ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo']},
        {name: 'editing',items: ['Find', 'Replace', '-', 'SelectAll', '-']},
        { name: 'basicstyles',items: ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat']},
        '/',
        {name: 'paragraph',items: ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl']},
        {name: 'links',items: ['Link', 'Unlink', 'Anchor']},
        {name: 'insert',items: ['Image', 'Flash', 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak']},
        '/',
        { name: 'styles',items: ['Styles', 'Format', 'Font', 'FontSize']},
        { name: 'colors',items: ['TextColor', 'BGColor']},
        { name: 'tools',items: [ 'ShowBlocks', '-']}
    ];
    config.toolbar_Basic = [['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink', '-', 'About']];
};
