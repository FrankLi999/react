'use client'
import React from 'react'
import baseCmsApiUrl from '@/utils/cms'
import {
    Container,
    Card,
    Col,
    Row
} from "react-bootstrap";
import Link from 'next/link';
function AboutIntegrators() {
    // About Us API
    const [about, setAbout] = React.useState<any>()
    React.useEffect(() => {
    const getAbout = async () => {
    //   const response = await axios.get(
    //     `${baseCmsApiUrl}/api/technology-solutions-about-us?populate=deep`,
    //   )
    //   setAbout(response.data)
      const response = await fetch(`${baseCmsApiUrl}/api/technology-solutions-about-us?populate=deep`);
      setAbout(await response.json())
      console.log("about content", await response.json());
    }
    getAbout()
  }, [])

  // Award API
  const [award, setAward] = React.useState<any>()
  React.useEffect(() => {
    const getAward = async () => {
      //   const response = await axios.get(
      //     `${baseCmsApiUrl}/api/award?populate=deep`,
      //   )
      // setAward(response.data)
      const response = await fetch(`${baseCmsApiUrl}/api/award?populate=deep`);
      setAward(await response.json())  
      console.log("award content", response.json());
    }
    getAward()
  }, [])

    return (

        <>
            <Container>
                <Row className="justify-content-center">
                    <Col lg="10" md="8">
                        <Card>
                            <Card.Header>
                                <Card.Title as="h4" className="text-center font-weight-light my-4">About Integrator</Card.Title>
                            </Card.Header>
                            <Card.Body>
                                <div className="text-center">
                                {about && (
                                    <div className="row align-items-center">
                                    <div className="col-lg-6 col-md-12">
                                        <div className="about-image">
                                        <img 
                                            src={about.data.attributes.image.data.attributes.url}
                                            alt={about.data.attributes.image.data.attributes.alternativeText}
                                        />
                                        <div className="shape5">
                                            <img src="/images/shape/shape5.png" alt="image" />
                                        </div>
                                        </div>
                                    </div>
                                    <div className="col-lg-6 col-md-12">
                                        <div className="about-content">
                                        <span className="sub-title">
                                            {about.data.attributes.subTitle}
                                        </span>
                                        <h2>{about.data.attributes.title}</h2>
                                        <p>
                                            {about.data.attributes.shortDescription}
                                        </p>

                                        <ul className="features-list">
                                            {about.data.attributes.featuresList.map((feature) => (
                                            <li key={feature.id}>
                                                <div className="d-flex align-items-center">
                                                <div className="icon">
                                                    <img 
                                                    src={feature.icon.data.attributes.url}
                                                    alt={feature.icon.data.attributes.alternativeText}
                                                    />
                                                </div>
                                                <span>{feature.title}</span>
                                                </div>
                                            </li>
                                            ))}
                                        </ul>
                                        </div>
                                    </div>
                                    </div>
                                )}

                                {award && (
                                    <div className="awards-list">
                                    <h4>{award.data.attributes.title}</h4>

                                    <div className="row justify-content-center">
                                        {award.data.attributes.awardItems.map((items) => (
                                        <div className="col-lg-2 col-4 col-sm-4 col-md-3" key={items.id}>
                                            <div className="awards-box">
                                            <img 
                                                src={items.image.data.attributes.url}
                                                alt={items.image.data.attributes.alternativeText}
                                            />
                                            </div>
                                        </div>
                                        ))}
                                    </div>
                                    </div>
                                )}
                                </div>
                            </Card.Body>
                            <Card.Footer className="card-footer text-center">
                                <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                                    <Link href={`/integrator/configuration-data`}> Go to configurations </Link>
                                </div>                                
                            </Card.Footer>
                        </Card>

                    </Col>
                </Row>
            </Container>            
        </>

    );

}
export default AboutIntegrators;