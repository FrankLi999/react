import { Button, Modal, ModalVariant } from '@patternfly/react-core';
import { DeleteModalProp } from './ConfigurationModel';

const DeleteConfirmation = ({
  showModal,
  hideModal,
  confirmModal,
  row,
  message,
}: DeleteModalProp) => {
  return (
    <Modal
      bodyAriaLabel='Scrollable modal content'
      tabIndex={0}
      variant={ModalVariant.medium}
      title='Delete Confirmation'
      isOpen={showModal}
      onClose={hideModal}
      actions={[
        <Button
          variant='danger'
          onClick={() => confirmModal(row)}
        >
          Delete
        </Button>,
        <Button
          variant='secondary'
          onClick={hideModal}
        >
          Cancel
        </Button>,
      ]}
    >
      <div className='alert alert-danger'>{message}</div>
    </Modal>
  );
};

export default DeleteConfirmation;
