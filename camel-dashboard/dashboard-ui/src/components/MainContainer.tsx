import FormComponent from './FormComponent';
import React from 'react';
import "./compStyling/MainContainer.css";

function MainContainer(){
    return(
        <div className="parentContainer">
            <FormComponent/>
        </div>
    );
}

export default MainContainer;