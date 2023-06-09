package ocp.maven.plugin.model;

import ocp.maven.plugin.helm.model.Chart;
import ocp.maven.plugin.helm.model.Repository;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ocp.maven.plugin.helm.CommandType;

import junit.framework.Assert;

public class ChartTest {

	@Test
	public void givenHttpRepository_thenReturnHttpRepositoryCommandType() {
		Repository repo = new Repository(null, "https://charts.example.com", null, null);
		Chart chart = new Chart("nginx", null, repo);
		Assert.assertEquals(CommandType.UPGRADE_FROM_HTTP_REPOSITORY, chart.getUpgradeType());
	}
	
	@Test
	public void givenOciRegistry_thenReturnOciRegistryCommandType() {
		Repository repo = new Repository(null, "oci://charts.example.com", null, null);
		Chart chart = new Chart("nginx", null, repo);
		Assert.assertEquals(CommandType.UPGRADE_FROM_OCI_REGISTRY, chart.getUpgradeType());
	}
	
	@Test
	public void givenAddedRepository_thenReturnAddedRepositoryCommandType() {
		Repository repo = new Repository("test", null, null, null);
		Chart chart = new Chart("nginx", null, repo);
		Assert.assertEquals(CommandType.UPGRADE_FROM_ADDED_REPOSITORY, chart.getUpgradeType());
	}
	
	@Test
	public void givenLocalChart_thenReturnLocalCommandType() {
		Repository repo = new Repository(null, "./test-chart", null, null);
		Chart chart = new Chart(null, null, repo);
		Assert.assertEquals(CommandType.UPGRADE_FROM_LOCAL, chart.getUpgradeType());
	}
	
	@Test
	public void givenChartWithNoName_whenRepositoryIsLocal_thenChartIsValid() {
		Repository repo = new Repository(null, "./test-chart", null, null);
		Chart chart = new Chart(null, null, repo);
		Assertions.assertDoesNotThrow(() -> {
			chart.validate();
		});
	}
	
	@Test
	public void givenChartWithName_whenRepositoryIsLocal_thenChartIsNotValid() {
		Repository repo = new Repository(null, "./test-chart", null, null);
		Chart chart = new Chart("nginx", null, repo);
		Assertions.assertThrows(MojoExecutionException.class, () -> {
			chart.validate();
		});
	}
	
	@Test
	public void givenChartWithNoName_whenRepositoryIsHttpRepository_thenChartIsNotValid() {
		Repository repo = new Repository(null, "https://charts.example.com", null, null);
		Chart chart = new Chart(null, null, repo);
		Assertions.assertThrows(MojoExecutionException.class, () -> {
			chart.validate();
		});
	}
	
	@Test
	public void givenChartWithNoName_whenRepositoryIsOciRegistry_thenChartIsNotValid() {
		Repository repo = new Repository(null, "oci://charts.example.com", null, null);
		Chart chart = new Chart(null, null, repo);
		Assertions.assertThrows(MojoExecutionException.class, () -> {
			chart.validate();
		});
	}
	
	@Test
	public void givenChartWithNoName_whenRepositoryIsAddedRepository_thenChartIsNotValid() {
		Repository repo = new Repository("test", null, null, null);
		Chart chart = new Chart(null, null, repo);
		Assertions.assertThrows(MojoExecutionException.class, () -> {
			chart.validate();
		});
	}
	
	@Test
	public void givenLocalChart_whenUsernameOrPasswordIsProvided_thenConfigIsNotValid() {
		Repository repo = new Repository(null, "./test-chart", "username", "password");
		Chart chart = new Chart(null, null, repo);
		Assertions.assertThrows(MojoExecutionException.class, () -> {
			chart.validate();
		});
	}
	
	@Test
	public void givenAddedChart_whenUsernameOrPasswordIsProvided_thenConfigIsNotValid() {
		Repository repo = new Repository("test", null, "username", "password");
		Chart chart = new Chart("nginx", null, repo);
		Assertions.assertThrows(MojoExecutionException.class, () -> {
			chart.validate();
		});
	}
	
	@Test
	public void givenOciRegistry_whenUsernameOrPasswordIsProvided_thenConfigIsNotValid() {
		Repository repo = new Repository(null, "oci://charts.example.com", "username", "password");
		Chart chart = new Chart("nginx", null, repo);
		Assertions.assertThrows(MojoExecutionException.class, () -> {
			chart.validate();
		});
	}
}
