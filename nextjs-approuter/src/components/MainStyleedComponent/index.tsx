import React from 'react';
import * as S from './styles';

const MainStyleedComponent = ({
	title = 'React Avançado',
	description = 'TypeScript, ReactJS, NextJS e Styled Components'
}) => {
	return (
		<S.Wrapper>
			<S.Logo
				src="/img/logo.svg"
				width={25}
				height={25}
				alt="Imagem de um átomo e React Avançado escrito ao lado."
			/>
			<S.Title>{title}</S.Title>
			<S.Description>{description}</S.Description>
			<S.Illustration
				src="/img/hero-illustration.svg"
				width={30}
				height={30}
				alt="Um desenvolvedor de frente para uma tela com código."
			/>

		</S.Wrapper>
	);
};

export default MainStyleedComponent;