
import { useRef, useState } from "react";
import { Modal, ModalVariant, Button } from '@patternfly/react-core';
import { ImportModalProp } from "./ConfigurationModel";

const ImportConfiguration = ({ showModal, hideModal, importConfiguration }: ImportModalProp) => {
  const [uploadedFile, setUploadedFile] = useState<File|null>(null);  
  const inputRef = useRef<HTMLInputElement>(null);
    
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
    <Modal
        bodyAriaLabel="Scrollable modal content"
        tabIndex={0}
        variant={ModalVariant.medium}
        title="Import Spring Configurations"
        isOpen={showModal}
        onClose={hideModal}
        actions={[
          <Button variant="primary" isDisabled={!uploadedFile} onClick={(event) => {event.preventDefault(); importConfigurationData();} }>
            Import
          </Button>,
          <Button variant="secondary" onClick={hideModal}>
            Cancel
          </Button>
      ]}
    >
      <label htmlFor="name" className="mx-3">Choose file: </label>
      <input id="file" ref={inputRef} onChange={handleDisplayFileDetails} className="d-none" type="file" />
      <button onClick={handleUpload} className={`btn btn-outline-${uploadedFile ? "success" : "primary"}`}>
        {uploadedFile ? uploadedFile.name : "Upload"}
      </button>
    </Modal>
  )
}

export default ImportConfiguration;