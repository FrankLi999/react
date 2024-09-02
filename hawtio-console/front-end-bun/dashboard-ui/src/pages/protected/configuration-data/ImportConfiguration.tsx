
import { useRef, useState } from "react";
import { Modal, Button } from "react-bootstrap";

const ImportConfiguration = ({ showModal, hideModal, importConfiguration }) => {
    const inputRef = useRef<HTMLInputElement>(null);
    const [uploadedFile, setUploadedFile] = useState<File|null>(null);
    const handleUpload = () => {
        inputRef.current?.click();
    };
    const handleDisplayFileDetails = () => {
        inputRef.current?.files && setUploadedFile(inputRef.current.files[0]);
    };

    async function  importConfigurationData() {
        uploadedFile && importConfiguration(uploadedFile);
    }
    return (
        <Modal show={showModal} onHide={hideModal}>
        <Modal.Header closeButton>
          <Modal.Title>Import Spring Configurations</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <label className="mx-3">Choose file: </label>
            <input ref={inputRef} onChange={handleDisplayFileDetails} className="d-none" type="file" />
            <button
                onClick={handleUpload}
                className={`btn btn-outline-${
                uploadedFile ? "success" : "primary"
                }`}
            >
                {uploadedFile ? uploadedFile.name : "Upload"}
            </button>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="default" onClick={hideModal}>
            Cancel
          </Button>
          {uploadedFile &&
            <Button variant="danger" onClick={(event) => {event.preventDefault(); importConfigurationData();} }>
              Import
            </Button>
          }
        </Modal.Footer>
      </Modal>
    )
}
 
export default ImportConfiguration;