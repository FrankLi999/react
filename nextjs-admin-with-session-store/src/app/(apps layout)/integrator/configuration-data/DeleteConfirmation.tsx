'use client'
import { ConfigurationModel } from "@/type/ConfigurationModel";
import { Modal, Button } from "react-bootstrap";
 
const DeleteConfirmation = ({ showModal, hideModal, confirmModal, row, message }: {
  showModal: boolean;
  hideModal: () => void;
  confirmModal: (row: ConfigurationModel|null) => void;
  row: ConfigurationModel | null;
  message: string;
}) => {
    return (
        <Modal show={showModal} onHide={hideModal}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body><div className="alert alert-danger">{message}</div></Modal.Body>
        <Modal.Footer>
          <Button variant="default" onClick={hideModal}>
            Cancel
          </Button>
          <Button variant="danger" onClick={() => confirmModal(row) }>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>
    )
}
 
export default DeleteConfirmation;
