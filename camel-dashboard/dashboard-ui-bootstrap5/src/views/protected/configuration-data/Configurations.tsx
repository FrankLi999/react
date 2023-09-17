import React, { useEffect, useState } from 'react';

function Configurations() {
    const [configurations, setconfigurations] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
    
        fetch('api/configurations')
          .then(response => response.json())
          .then(data => {
            setconfigurations(data);
            setLoading(false);
          })
      }, []);
    
      if (loading) {
        return <p>Loading...</p>;
      }

      return (
        <div className="App">
          <header className="App-header">
            <div className="App-intro">
              <h2>JUG List</h2>
              {configurations.map(group =>
                <div key={group.id}>
                  {group.name}
                </div>
              )}
            </div>
          </header>
        </div>
      );
}      

export default Configurations;