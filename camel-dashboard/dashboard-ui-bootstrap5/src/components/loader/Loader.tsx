// import { useEffect } from 'react';
import loader from '../../assets/img/loader.svg';
const Loader = () => {
    // useEffect(() => {
    //   console.log("Working");
      
    // });
    
    return (
        <div id="loader">
            <img src={loader} className="loader-img" alt="Loader" />
        </div>
    );
};

export default Loader;